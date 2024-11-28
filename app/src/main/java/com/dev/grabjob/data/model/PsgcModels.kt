package com.dev.grabjob.data.model

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("regionName")
    val regionName: String,

    @SerializedName("islandGroupCode")
    val islandGroupCode: String,

    @SerializedName("psgc10DigitCode")
    val psgc10DigitCode: String
)

data class Province(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("regionCode")
    val regionCode: String,
    
    @SerializedName("islandGroupCode")
    val islandGroupCode: String,
    
    @SerializedName("psgc10DigitCode")
    val psgc10DigitCode: String
)

data class Municipality(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("provinceCode")
    val provinceCode: String,
    
    @SerializedName("psgc10DigitCode")
    val psgc10DigitCode: String,
    
    @SerializedName("cityClass")
    val cityClass: String? = null,
    
    @SerializedName("isCity")
    val isCity: Boolean = false
)

data class City(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("provinceCode")
    val provinceCode: String,
    
    @SerializedName("psgc10DigitCode")
    val psgc10DigitCode: String,
    
    @SerializedName("cityClass")
    val cityClass: String,
    
    @SerializedName("districtCode")
    val districtCode: String? = null
)

data class Barangay(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("cityCode")
    val cityCode: String? = null,
    
    @SerializedName("municipalityCode")
    val municipalityCode: String? = null,
    
    @SerializedName("psgc10DigitCode")
    val psgc10DigitCode: String
)
