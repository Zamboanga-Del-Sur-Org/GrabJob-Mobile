package com.dev.grabjob.ui.screens.registration.models

import com.dev.grabjob.data.model.Region
import com.dev.grabjob.data.model.Province
import com.dev.grabjob.data.model.City
import com.dev.grabjob.data.model.Barangay
import com.dev.grabjob.data.model.Municipality
import java.time.LocalDate

data class RegistrationUiState(
    val registrationData: RegistrationData = RegistrationData(),
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistrationComplete: Boolean = false
)

data class AddressUiState(
    val regions: List<Region> = emptyList(),
    val provinces: List<Province> = emptyList(),
    val citiesAndMunicipalities: List<Municipality> = emptyList(),
    val barangays: List<Barangay> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingProvinces: Boolean = false,
    val isLoadingCitiesAndMunicipalities: Boolean = false,
    val isLoadingBarangays: Boolean = false,
    val error: String? = null
)

data class RegistrationData(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: LocalDate? = null,
    val region: String = "",
    val province: String = "",
    val municipality: String = "",
    val city: String = "",
    val barangay: String = "",
    val street: String = "",
    val attachments: List<String> = emptyList(),
    val errors: Map<String, String> = emptyMap()
)

sealed class RegistrationEvent {
    data class UpdateFirstName(val value: String) : RegistrationEvent()
    data class UpdateLastName(val value: String) : RegistrationEvent()
    data class UpdateBirthDate(val value: LocalDate?) : RegistrationEvent()
    data class UpdateRegion(val value: String) : RegistrationEvent()
    data class UpdateProvince(val value: String) : RegistrationEvent()
    data class UpdateLocation(val value: String) : RegistrationEvent()
    data class UpdateBarangay(val value: String) : RegistrationEvent()
    data class UpdateStreet(val value: String) : RegistrationEvent()
    data class UpdateEmail(val value: String) : RegistrationEvent()
    data class UpdatePassword(val value: String) : RegistrationEvent()
    data class UpdateConfirmPassword(val value: String) : RegistrationEvent()
    data class UpdateAttachments(val attachments: List<String>) : RegistrationEvent()
    object Submit : RegistrationEvent()
    object NextStep : RegistrationEvent()
    object PreviousStep : RegistrationEvent()
    object ClearError : RegistrationEvent()
}
