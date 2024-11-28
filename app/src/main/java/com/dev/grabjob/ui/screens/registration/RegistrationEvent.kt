package com.dev.grabjob.ui.screens.registration.RegistrationEvent

sealed class RegistrationEvent {
    data class UpdateFirstName(val value: String) : RegistrationEvent()
    data class UpdateLastName(val value: String) : RegistrationEvent()
    data class UpdateBirthDate(val value: Long) : RegistrationEvent()
    data class UpdateProvince(val value: String) : RegistrationEvent()
    data class UpdateCity(val value: String) : RegistrationEvent()
    data class UpdateBarangay(val value: String) : RegistrationEvent()
    data class UpdateStreet(val value: String) : RegistrationEvent()
    data class UpdateUsername(val value: String) : RegistrationEvent()
    data class UpdatePassword(val value: String) : RegistrationEvent()
    data class UpdateConfirmPassword(val value: String) : RegistrationEvent()
    data class UpdateAttachments(val value: List<String>) : RegistrationEvent()
    object Submit : RegistrationEvent()
    object NextStep : RegistrationEvent()
    object PreviousStep : RegistrationEvent()
    object ClearError : RegistrationEvent()
}