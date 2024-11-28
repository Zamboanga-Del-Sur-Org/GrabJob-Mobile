package com.dev.grabjob.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface AddressApi {
    @GET("provinces")
    suspend fun getProvinces(): List<String>

    @GET("cities/{province}")
    suspend fun getCitiesByProvince(@Path("province") province: String): List<String>

    @GET("barangays/{city}")
    suspend fun getBarangaysByCity(@Path("city") city: String): List<String>
}
