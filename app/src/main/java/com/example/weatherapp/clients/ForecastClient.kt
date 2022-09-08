package com.example.weatherapp.clients

import com.example.weatherapp.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastClient @Inject constructor(private val service: ApiService.IApiService) {
    fun getForecast() = service.getForecast()
    fun getCurrentWeather() = service.getCurrentWeather()
}