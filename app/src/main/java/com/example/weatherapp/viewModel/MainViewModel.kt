package com.example.weatherapp.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.api.response.ApiList
import com.example.weatherapp.clients.ForecastClient


class MainViewModel(private val forecastClient: ForecastClient) : ViewModel() {

    val currentTemperature = ObservableField<String>()
    val minimumTemperature = ObservableField<Int>()
    val maximumTemperature = ObservableField<Int>()
    val forecasts = ObservableField<List<ApiList>>()

    fun initiate() {
        currentTemperature.set("26")
    }

    /*private fun getForecast() {
        forecastClient.getForecast().subscribe(
            {
            forecasts.set(it.list)
            },
            {

            }
        )
    }*/

}