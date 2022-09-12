package com.example.weatherapp.clients

import com.example.weatherapp.api.ApiService
import com.example.weatherapp.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherClient @Inject constructor(private val service: ApiService.IApiService) {

    fun getWeather(latitude: Double, longitude: Double) =
        service.getWeather(latitude, longitude, Constants.APP_ID, Constants.UNITS)
}