package com.dev.grabjob.data.api

import com.dev.grabjob.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path

interface PsgcApiService {
    @GET("regions")
    suspend fun getRegions(): List<Region>
    
    @GET("provinces")
    suspend fun getProvinces(): List<Province>
    
    @GET("provinces/{provinceCode}/cities-municipalities")
    suspend fun getCitiesAndMunicipalities(@Path("provinceCode") provinceCode: String): List<Municipality>
    
    @GET("cities/{cityCode}/barangays")
    suspend fun getBarangaysByCity(@Path("cityCode") cityCode: String): List<Barangay>
    
    @GET("municipalities/{municipalityCode}/barangays")
    suspend fun getBarangaysByMunicipality(@Path("municipalityCode") municipalityCode: String): List<Barangay>
}
