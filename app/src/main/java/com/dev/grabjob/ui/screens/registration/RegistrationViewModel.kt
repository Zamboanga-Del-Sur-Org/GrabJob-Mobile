package com.dev.grabjob.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.grabjob.data.api.PsgcApiService
import com.dev.grabjob.data.api.RegistrationRequest
import com.dev.grabjob.data.repository.AddressRepository
import com.dev.grabjob.data.repository.AuthRepository
import com.dev.grabjob.ui.screens.registration.models.AddressUiState
import com.dev.grabjob.ui.screens.registration.models.RegistrationData
import com.dev.grabjob.ui.screens.registration.models.RegistrationEvent
import com.dev.grabjob.ui.screens.registration.models.RegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val psgcApiService: PsgcApiService,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _addressState = MutableStateFlow(AddressUiState())
    val addressState: StateFlow<AddressUiState> = _addressState.asStateFlow()

    init {
        viewModelScope.launch {
            _addressState.update { it.copy(isLoading = true) }
            try {
                val regions = psgcApiService.getRegions()
                _addressState.update { it.copy(
                    regions = regions,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _addressState.update { it.copy(
                    error = "Failed to load regions",
                    isLoading = false
                ) }
            }
        }
    }

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.UpdateFirstName -> updateRegistrationData { it.copy(firstName = event.value) }
            is RegistrationEvent.UpdateLastName -> updateRegistrationData { it.copy(lastName = event.value) }
            is RegistrationEvent.UpdateEmail -> updateRegistrationData { it.copy(email = event.value) }
            is RegistrationEvent.UpdatePassword -> updateRegistrationData { it.copy(password = event.value) }
            is RegistrationEvent.UpdateConfirmPassword -> updateRegistrationData { it.copy(confirmPassword = event.value) }
            is RegistrationEvent.UpdateBirthDate -> updateRegistrationData { it.copy(birthDate = event.value) }
            is RegistrationEvent.UpdateRegion -> {
                val region = _addressState.value.regions.find { it.code == event.value }
                println("Selected Region: ${region?.name}, Code: ${region?.code}")
                updateRegistrationData { it.copy(
                    region = event.value,
                    province = "", // Clear dependent fields
                    municipality = "",
                    city = "",
                    barangay = ""
                ) }
                // Clear dependent lists
                _addressState.update { it.copy(
                    provinces = emptyList(),
                    citiesAndMunicipalities = emptyList(),
                    barangays = emptyList(),
                    error = null // Clear any previous errors
                ) }
                // Load provinces for selected region
                viewModelScope.launch {
                    try {
                        loadProvinces(event.value)
                    } catch (e: Exception) {
                        println("Error in coroutine while loading provinces: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
            is RegistrationEvent.UpdateProvince -> {
                val province = _addressState.value.provinces.find { it.code == event.value }
                println("Selected Province: ${province?.name}, Code: ${province?.code}")
                updateRegistrationData { it.copy(
                    province = event.value,
                    city = "", // Clear dependent fields
                    municipality = "",
                    barangay = ""
                ) }
                // Clear dependent lists
                _addressState.update { it.copy(
                    citiesAndMunicipalities = emptyList(),
                    barangays = emptyList()
                ) }
                // Load cities and municipalities for selected province
                viewModelScope.launch {
                    try {
                        loadCitiesAndMunicipalities(event.value)
                    } catch (e: Exception) {
                        println("Error in coroutine while loading cities and municipalities: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
            is RegistrationEvent.UpdateLocation -> {
                val location = _addressState.value.citiesAndMunicipalities.find { it.code == event.value }
                println("Selected Location: ${location?.name}, Code: ${location?.code}, Is City: ${location?.isCity}")
                updateRegistrationData { it.copy(
                    city = if (location?.isCity == true) event.value else "",
                    municipality = if (location?.isCity == false) event.value else "",
                    barangay = ""
                ) }
                // Clear dependent list
                _addressState.update { it.copy(
                    barangays = emptyList(),
                    error = null
                ) }
                // Load barangays for selected location
                viewModelScope.launch {
                    try {
                        location?.let { loc ->
                            loadBarangays(loc.code, loc.isCity)
                        }
                    } catch (e: Exception) {
                        println("Error in coroutine while loading barangays: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
            is RegistrationEvent.UpdateBarangay -> {
                val barangay = _addressState.value.barangays.find { it.code == event.value }
                println("Selected Barangay: ${barangay?.name}, Code: ${barangay?.code}")
                updateRegistrationData { it.copy(barangay = event.value) }
            }
            is RegistrationEvent.UpdateStreet -> updateRegistrationData { it.copy(street = event.value) }
            is RegistrationEvent.UpdateAttachments -> updateRegistrationData { it.copy(attachments = event.attachments) }
            is RegistrationEvent.Submit -> submitRegistration()
            is RegistrationEvent.NextStep -> moveToNextStep()
            is RegistrationEvent.PreviousStep -> moveToPreviousStep()
            is RegistrationEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
                _addressState.update { it.copy(error = null) }
            }
        }
    }

    private suspend fun loadProvinces(regionCode: String) {
        println("Loading provinces for region code: $regionCode")
        _addressState.update { it.copy(isLoadingProvinces = true) }
        try {
            println("Making API call to fetch all provinces")
            val allProvinces = psgcApiService.getProvinces()
            println("Successfully fetched ${allProvinces.size} provinces")
            // Filter provinces by region code
            val filteredProvinces = allProvinces.filter { it.regionCode == regionCode }
            println("Filtered to ${filteredProvinces.size} provinces for region $regionCode")
            
            _addressState.update { it.copy(
                provinces = filteredProvinces,
                isLoadingProvinces = false,
                error = null
            ) }
        } catch (e: Exception) {
            println("Error loading provinces: ${e.message}")
            e.printStackTrace() // Print the full stack trace
            _addressState.update { it.copy(
                error = "Failed to load provinces: ${e.message}",
                isLoadingProvinces = false,
                provinces = emptyList()
            ) }
        }
    }

    private suspend fun loadCitiesAndMunicipalities(provinceCode: String) {
        println("Loading cities and municipalities for province code: $provinceCode")
        _addressState.update { it.copy(isLoadingCitiesAndMunicipalities = true) }
        try {
            val locations = psgcApiService.getCitiesAndMunicipalities(provinceCode)
            println("Successfully fetched ${locations.size} locations")
            _addressState.update { it.copy(
                citiesAndMunicipalities = locations,
                isLoadingCitiesAndMunicipalities = false,
                error = null
            ) }
        } catch (e: Exception) {
            println("Error loading cities and municipalities: ${e.message}")
            e.printStackTrace()
            _addressState.update { it.copy(
                error = "Failed to load cities and municipalities: ${e.message}",
                isLoadingCitiesAndMunicipalities = false,
                citiesAndMunicipalities = emptyList()
            ) }
        }
    }

    private suspend fun loadBarangays(locationCode: String, isCity: Boolean) {
        println("Loading barangays for ${if (isCity) "city" else "municipality"} code: $locationCode")
        _addressState.update { it.copy(isLoadingBarangays = true) }
        try {
            val barangays = if (isCity) {
                psgcApiService.getBarangaysByCity(locationCode)
            } else {
                psgcApiService.getBarangaysByMunicipality(locationCode)
            }
            println("Successfully fetched ${barangays.size} barangays")
            _addressState.update { it.copy(
                barangays = barangays,
                isLoadingBarangays = false,
                error = null
            ) }
        } catch (e: Exception) {
            println("Error loading barangays: ${e.message}")
            e.printStackTrace()
            _addressState.update { it.copy(
                error = "Failed to load barangays: ${e.message}",
                isLoadingBarangays = false,
                barangays = emptyList()
            ) }
        }
    }

    private fun updateRegistrationData(update: (RegistrationData) -> RegistrationData) {
        _uiState.update { it.copy(registrationData = update(it.registrationData)) }
    }

    private fun validateCurrentStep(): Boolean {
        val currentStep = _uiState.value.currentStep
        val data = _uiState.value.registrationData
        val errors = mutableMapOf<String, String>()

        when (currentStep) {
            0 -> validatePersonalInfo(data)
            1 -> { // Address
                if (data.province.isBlank()) {
                    errors["province"] = "Province is required"
                }
                if (data.municipality.isBlank() && data.city.isBlank()) {
                    errors["city"] = "City or municipality is required"
                }
                if (data.barangay.isBlank()) {
                    errors["barangay"] = "Barangay is required"
                }
                if (data.street.isBlank()) {
                    errors["street"] = "Street address is required"
                }
            }
            2 -> { // Credentials
                if (data.email.isBlank()) {
                    errors["email"] = "Email is required"
                }
                if (data.password.length < 8) {
                    errors["password"] = "Password must be at least 8 characters"
                }
                if (data.password != data.confirmPassword) {
                    errors["confirmPassword"] = "Passwords do not match"
                }
            }
        }

        _uiState.update { it.copy(registrationData = it.registrationData.copy(errors = errors)) }
        return errors.isEmpty()
    }

    private fun validatePersonalInfo(data: RegistrationData): Boolean {
        val errors = mutableMapOf<String, String>()

        if (data.firstName.isBlank()) {
            errors["firstName"] = "First name is required"
        }
        if (data.lastName.isBlank()) {
            errors["lastName"] = "Last name is required"
        }
        if (data.birthDate == null) {
            errors["birthDate"] = "Birth date is required"
        } else {
            val age = Period.between(data.birthDate, LocalDate.now()).years
            if (age !in 18..100) {
                errors["birthDate"] = "Age must be between 18 and 100 years"
            }
        }

        updateRegistrationData { it.copy(errors = errors) }
        return errors.isEmpty()
    }

    private fun moveToNextStep() {
        if (validateCurrentStep()) {
            _uiState.update { it.copy(currentStep = it.currentStep + 1) }
        }
    }

    private fun moveToPreviousStep() {
        _uiState.update { it.copy(currentStep = it.currentStep - 1) }
    }

    private fun submitRegistration() {
        val registrationData = _uiState.value.registrationData
        val birthDateTimestamp = registrationData.birthDate?.toEpochDay() ?: return
        
        viewModelScope.launch {
            try {
                val request = RegistrationRequest(
                    firstName = registrationData.firstName,
                    lastName = registrationData.lastName,
                    birthDate = birthDateTimestamp,
                    province = registrationData.province,
                    city = registrationData.city,
                    barangay = registrationData.barangay,
                    street = registrationData.street,
                    username = registrationData.email, // Using email as username
                    password = registrationData.password,
                    attachments = registrationData.attachments
                )
                
                authRepository.register(request).fold(
                    onSuccess = { response ->
                        _uiState.update { it.copy(
                            isRegistrationComplete = true,
                            error = null
                        ) }
                    },
                    onFailure = { exception ->
                        _uiState.update { it.copy(
                            error = exception.message ?: "Registration failed"
                        ) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = "Registration failed: ${e.message}"
                ) }
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
        _addressState.update { it.copy(error = null) }
    }
}
