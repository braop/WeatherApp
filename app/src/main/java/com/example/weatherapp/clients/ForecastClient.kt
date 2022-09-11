package com.example.weatherapp.clients

import com.example.weatherapp.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastClient @Inject constructor(private val service: ApiService.IApiService) {

    fun getForecast(latitude: Double, longitude: Double, appId: String, units: String) =
        service.getForecast(latitude, longitude, appId, units)

    fun getWeather(latitude: Double, longitude: Double, appId: String, units: String) =
        service.getWeather(latitude, longitude, appId, units)
}