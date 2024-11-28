package com.dev.grabjob.data.repository

import com.dev.grabjob.data.api.AddressApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val addressApi: AddressApi
) {
    suspend fun getProvinces(): List<String> = withContext(Dispatchers.IO) {
        addressApi.getProvinces()
    }

    suspend fun getCitiesByProvince(province: String): List<String> = withContext(Dispatchers.IO) {
        addressApi.getCitiesByProvince(province)
    }

    suspend fun getBarangaysByCity(city: String): List<String> = withContext(Dispatchers.IO) {
        addressApi.getBarangaysByCity(city)
    }
}
