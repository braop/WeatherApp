package com.example.weatherapp.api.response

data class ApiForecast(
    var cod: String?,
    var message: Int?,
    var cnt: Int?,
    var list: List<ApiList>?,
    var city: ApiCity?
)
