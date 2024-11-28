package com.dev.grabjob.ui.screens.registration


import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.grabjob.ui.screens.registration.models.AddressUiState
import com.dev.grabjob.ui.screens.registration.models.RegistrationData
import com.dev.grabjob.ui.screens.registration.models.RegistrationEvent
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegistrationComplete: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val addressState by viewModel.addressState.collectAsState()

    LaunchedEffect(uiState.isRegistrationComplete) {
        if (uiState.isRegistrationComplete) {
            onRegistrationComplete()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Processing registration...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    uiState.error?.let { error ->
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(RegistrationEvent.ClearError)
            },
            title = {
                Text("Error")
            },
            text = {
                Text(error)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(RegistrationEvent.ClearError)
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ) {
                Column {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Create Account",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Step ${uiState.currentStep + 1} of 4",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    
                    LinearProgressIndicator(
                        progress = (uiState.currentStep + 1) / 4f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = when (uiState.currentStep) {
                                0 -> "Personal Information"
                                1 -> "Address Details"
                                2 -> "Account Credentials"
                                else -> "Required Documents"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Step content with animation
                        AnimatedContent(
                            targetState = uiState.currentStep,
                            transitionSpec = {
                                fadeIn() + slideInHorizontally() with 
                                fadeOut() + slideOutHorizontally()
                            }
                        ) { step ->
                            when (step) {
                                0 -> PersonalInfoStep(
                                    registrationData = uiState.registrationData,
                                    onDataChange = { newData ->
                                        when {
                                            newData.firstName != uiState.registrationData.firstName ->
                                                viewModel.onEvent(RegistrationEvent.UpdateFirstName(newData.firstName))
                                            newData.lastName != uiState.registrationData.lastName ->
                                                viewModel.onEvent(RegistrationEvent.UpdateLastName(newData.lastName))
                                            newData.birthDate != uiState.registrationData.birthDate ->
                                                viewModel.onEvent(RegistrationEvent.UpdateBirthDate(newData.birthDate))
                                        }
                                    },
                                    onEvent = viewModel::onEvent
                                )
                                1 -> AddressStep(
                                    registrationData = uiState.registrationData,
                                    addressState = addressState,
                                    onDataChange = { newData ->
                                        when {
                                            newData.province != uiState.registrationData.province ->
                                                viewModel.onEvent(RegistrationEvent.UpdateProvince(newData.province))
                                            newData.city != uiState.registrationData.city || 
                                            newData.municipality != uiState.registrationData.municipality ->
                                                viewModel.onEvent(RegistrationEvent.UpdateLocation(
                                                    if (newData.city.isNotBlank()) newData.city else newData.municipality
                                                ))
                                            newData.barangay != uiState.registrationData.barangay ->
                                                viewModel.onEvent(RegistrationEvent.UpdateBarangay(newData.barangay))
                                            newData.street != uiState.registrationData.street ->
                                                viewModel.onEvent(RegistrationEvent.UpdateStreet(newData.street))
                                        }
                                    },
                                    onEvent = viewModel::onEvent
                                )
                                2 -> CredentialsStep(
                                    registrationData = uiState.registrationData,
                                    onDataChange = { newData ->
                                        when {
                                            newData.email != uiState.registrationData.email ->
                                                viewModel.onEvent(RegistrationEvent.UpdateEmail(newData.email))
                                            newData.password != uiState.registrationData.password ->
                                                viewModel.onEvent(RegistrationEvent.UpdatePassword(newData.password))
                                            newData.confirmPassword != uiState.registrationData.confirmPassword ->
                                                viewModel.onEvent(RegistrationEvent.UpdateConfirmPassword(newData.confirmPassword))
                                        }
                                    },
                                    onEvent = viewModel::onEvent
                                )
                                3 -> AttachmentsStep(
                                    registrationData = uiState.registrationData,
                                    onDataChange = { newData ->
                                        viewModel.onEvent(RegistrationEvent.UpdateAttachments(newData.attachments))
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Navigation buttons with glassmorphism
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.currentStep > 0) {
                        OutlinedButton(
                            onClick = { viewModel.onEvent(RegistrationEvent.PreviousStep) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Previous")
                        }
                    }

                    Button(
                        onClick = {
                            if (uiState.currentStep == 3) {
                                viewModel.onEvent(RegistrationEvent.Submit)
                            } else {
                                viewModel.onEvent(RegistrationEvent.NextStep)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        )
                    ) {
                        Text(if (uiState.currentStep == 3) "Submit" else "Next")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            if (uiState.currentStep == 3) Icons.Default.Done else Icons.Default.ArrowForward,
                            contentDescription = if (uiState.currentStep == 3) "Submit" else "Next"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepIndicator(
    step: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (step <= currentStep)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        if (step < currentStep) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Completed",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text(
                text = (step + 1).toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (step <= currentStep)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    error: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = error != null,
            enabled = enabled,
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                focusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ),
            supportingText = error?.let {
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoStep(
    registrationData: RegistrationData,
    onDataChange: (RegistrationData) -> Unit,
    onEvent: (RegistrationEvent) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMMM dd, yyyy") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextFieldWithError(
            value = registrationData.firstName,
            onValueChange = { onEvent(RegistrationEvent.UpdateFirstName(it)) },
            label = "First Name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "First Name") },
            error = registrationData.errors["firstName"],
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextFieldWithError(
            value = registrationData.lastName,
            onValueChange = { onEvent(RegistrationEvent.UpdateLastName(it)) },
            label = "Last Name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Last Name") },
            error = registrationData.errors["lastName"],
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextFieldWithError(
            value = registrationData.birthDate?.format(dateFormatter) ?: "",
            onValueChange = { },
            label = "Birth Date",
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Birth Date") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                }
            },
            readOnly = true,
            error = registrationData.errors["birthDate"],
            modifier = Modifier.fillMaxWidth()
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = registrationData.birthDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
            )
            
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onEvent(RegistrationEvent.UpdateBirthDate(selectedDate))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    dateValidator = { timestamp ->
                        val date = Instant.ofEpochMilli(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        val age = Period.between(date, LocalDate.now()).years
                        age in 18..100
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressStep(
    registrationData: RegistrationData,
    addressState: AddressUiState,
    onDataChange: (RegistrationData) -> Unit,
    onEvent: (RegistrationEvent) -> Unit
) {
    var expandedRegion by remember { mutableStateOf(false) }
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedLocation by remember { mutableStateOf(false) }
    var expandedBarangay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expandedRegion,
                onExpandedChange = { expandedRegion = it }
            ) {
                OutlinedTextField(
                    value = addressState.regions.find { it.code == registrationData.region }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Region") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Region") },
                    trailingIcon = { 
                        if (addressState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !addressState.isLoading
                )
                
                if (!addressState.isLoading) {
                    ExposedDropdownMenu(
                        expanded = expandedRegion,
                        onDismissRequest = { expandedRegion = false }
                    ) {
                        addressState.regions.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region.name) },
                                onClick = {
                                    onEvent(RegistrationEvent.UpdateRegion(region.code))
                                    expandedRegion = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expandedProvince,
                onExpandedChange = { expandedProvince = it }
            ) {
                OutlinedTextField(
                    value = addressState.provinces.find { it.code == registrationData.province }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = registrationData.region.isNotBlank() && !addressState.isLoadingProvinces,
                    label = { Text("Province") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = "Province") },
                    trailingIcon = { 
                        if (addressState.isLoadingProvinces) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvince)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                if (!addressState.isLoadingProvinces && registrationData.region.isNotBlank()) {
                    ExposedDropdownMenu(
                        expanded = expandedProvince,
                        onDismissRequest = { expandedProvince = false }
                    ) {
                        addressState.provinces.forEach { province ->
                            DropdownMenuItem(
                                text = { Text(province.name) },
                                onClick = {
                                    onEvent(RegistrationEvent.UpdateProvince(province.code))
                                    expandedProvince = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expandedLocation,
                onExpandedChange = { expandedLocation = it }
            ) {
                OutlinedTextField(
                    value = addressState.citiesAndMunicipalities.find { 
                        it.code == (if (it.isCity) registrationData.city else registrationData.municipality)
                    }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = registrationData.province.isNotBlank() && !addressState.isLoadingCitiesAndMunicipalities,
                    label = { Text("City/Municipality") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = "Location") },
                    trailingIcon = { 
                        if (addressState.isLoadingCitiesAndMunicipalities) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                if (!addressState.isLoadingCitiesAndMunicipalities && registrationData.province.isNotBlank()) {
                    ExposedDropdownMenu(
                        expanded = expandedLocation,
                        onDismissRequest = { expandedLocation = false }
                    ) {
                        addressState.citiesAndMunicipalities.forEach { location ->
                            DropdownMenuItem(
                                text = { Text("${location.name} ${if (location.isCity) "(City)" else "(Municipality)"}") },
                                onClick = {
                                    onEvent(RegistrationEvent.UpdateLocation(location.code))
                                    expandedLocation = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = expandedBarangay,
                onExpandedChange = { expandedBarangay = it }
            ) {
                OutlinedTextField(
                    value = addressState.barangays.find { it.code == registrationData.barangay }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = (registrationData.city.isNotBlank() || registrationData.municipality.isNotBlank()) && !addressState.isLoadingBarangays,
                    label = { Text("Barangay") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Barangay") },
                    trailingIcon = { 
                        if (addressState.isLoadingBarangays) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedBarangay)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(8.dp)
                )
                
                if (!addressState.isLoadingBarangays && (registrationData.city.isNotBlank() || registrationData.municipality.isNotBlank())) {
                    ExposedDropdownMenu(
                        expanded = expandedBarangay,
                        onDismissRequest = { expandedBarangay = false }
                    ) {
                        addressState.barangays.forEach { barangay ->
                            DropdownMenuItem(
                                text = { Text(barangay.name) },
                                onClick = {
                                    onEvent(RegistrationEvent.UpdateBarangay(barangay.code))
                                    expandedBarangay = false
                                }
                            )
                        }
                    }
                }
            }
        }

        OutlinedTextFieldWithError(
            value = registrationData.street,
            onValueChange = { onEvent(RegistrationEvent.UpdateStreet(it)) },
            label = "Street Address",
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = "Street Address") },
            error = registrationData.errors["street"],
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialsStep(
    registrationData: RegistrationData,
    onDataChange: (RegistrationData) -> Unit,
    onEvent: (RegistrationEvent) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextFieldWithError(
            value = registrationData.email,
            onValueChange = { onEvent(RegistrationEvent.UpdateEmail(it)) },
            label = "Email",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            error = registrationData.errors["email"],
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextFieldWithError(
            value = registrationData.password,
            onValueChange = { onEvent(RegistrationEvent.UpdatePassword(it)) },
            label = "Password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            error = registrationData.errors["password"],
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextFieldWithError(
            value = registrationData.confirmPassword,
            onValueChange = { onEvent(RegistrationEvent.UpdateConfirmPassword(it)) },
            label = "Confirm Password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password") },
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    Icon(
                        if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
            error = registrationData.errors["confirmPassword"],
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AttachmentsStep(
    registrationData: RegistrationData,
    onDataChange: (RegistrationData) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Upload Documents",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add file upload functionality here
        Button(
            onClick = { /* Implement file picker */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Document")
        }
        
        // Display list of uploaded documents
        registrationData.attachments.forEach { attachment ->
            Text(attachment)
        }
    }
}

private fun validatePersonalInfo(data: RegistrationData): Boolean {
    val errors = mutableMapOf<String, String>()
    
    if (data.firstName.isBlank()) {
        errors["firstName"] = "First name is required"
    }
    if (data.lastName.isBlank()) {
        errors["lastName"] = "Last name is required"
    }
    
    return errors.isEmpty()
}

private fun validateAddress(data: RegistrationData): Boolean {
    val errors = mutableMapOf<String, String>()
    
    if (data.province.isBlank()) errors["province"] = "Province is required"
    if (data.city.isBlank()) errors["city"] = "City is required"
    if (data.barangay.isBlank()) errors["barangay"] = "Barangay is required"
    if (data.street.isBlank()) errors["street"] = "Street address is required"
    if (data.municipality.isBlank()) errors["municipality"] = "Municipality is required"
    
    return errors.isEmpty()
}

private fun validateCredentials(data: RegistrationData): Boolean {
    val errors = mutableMapOf<String, String>()
    
    if (data.email.length < 4) {
        errors["email"] = "Email must be at least 4 characters"
    }
    if (data.password.length < 8) {
        errors["password"] = "Password must be at least 8 characters"
    }
    if (data.password != data.confirmPassword) {
        errors["confirmPassword"] = "Passwords do not match"
    }
    
    return errors.isEmpty()
}
