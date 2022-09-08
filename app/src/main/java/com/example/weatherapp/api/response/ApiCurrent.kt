package com.example.weatherapp.api.response

data class ApiCurrent(
    var coord: ApiCoord?,
    var weather: List<ApiWeather>?,
    var base: String?,
    var main: ApiMain?,
    var visibility: Int?,
    var wind: ApiWind?,
    var clouds: ApiClouds?,
    var dt: Int?,
    var sys: ApiSys?,
    var timezone: Int?,
    var id: Int?,
    var name: String?,
    var cod: Int?
)
