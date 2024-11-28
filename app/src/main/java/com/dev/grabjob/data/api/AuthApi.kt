package com.dev.grabjob.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegistrationRequest): Response<RegistrationResponse>
}

data class RegistrationRequest(
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val province: String,
    val city: String,
    val barangay: String,
    val street: String,
    val username: String,
    val password: String,
    val attachments: List<String>
)

data class RegistrationResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val token: String? = null
)
