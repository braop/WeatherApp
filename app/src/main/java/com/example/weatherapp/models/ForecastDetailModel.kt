package com.example.weatherapp.models

data class ForecastDetailModel(
    var id: Int,
    var status: String?,
    var dateText: String?,
    var temp: Int?
)