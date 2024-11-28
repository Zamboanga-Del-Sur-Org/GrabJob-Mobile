package com.dev.grabjob.data.models

data class Region(
    val code: String,
    val name: String,
    val regionName: String
)

data class Province(
    val code: String,
    val name: String,
    val regionCode: String
)

data class City(
    val code: String,
    val name: String,
    val provinceCode: String
)

data class Barangay(
    val code: String,
    val name: String,
    val cityCode: String
)
