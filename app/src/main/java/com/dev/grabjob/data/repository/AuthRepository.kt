package com.dev.grabjob.data.repository

import com.dev.grabjob.data.api.AuthApi
import com.dev.grabjob.data.api.RegistrationRequest
import com.dev.grabjob.data.api.RegistrationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun register(request: RegistrationRequest): Result<RegistrationResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.register(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
