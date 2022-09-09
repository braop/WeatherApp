package com.example.weatherapp.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.activity.MainInterface
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.api.response.ApiList
import com.example.weatherapp.clients.ForecastClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(private val forecastClient: ForecastClient) : ViewModel() {

    val currentWeather = ObservableField<ApiCurrent>()
    val currentTemperature = ObservableField<String>()
    val minimumTemperature = ObservableField<Int>()
    val maximumTemperature = ObservableField<Int>()
    val forecasts = ObservableField<List<ApiList>>()
    var navigator: MainInterface? = null

    fun initiate(navigator: MainInterface) {
        this.navigator = navigator
        getForecast()
        getCurrentWeatherByLocation()
    }

    private fun getForecast() {
        forecastClient.getForecast().enqueue(object : Callback<ApiForecast> {
            override fun onResponse(call: Call<ApiForecast>, response: Response<ApiForecast>) {
                response.body()?.let {
                    forecasts.set(it.list)
                }
                // navigator?.onSuccess()
            }

            override fun onFailure(call: Call<ApiForecast>, t: Throwable) {
                navigator?.onError()
            }

        })
    }

    private fun getCurrentWeatherByLocation() {
        forecastClient.getCurrentWeather().enqueue(object : Callback<ApiCurrent> {
            override fun onResponse(call: Call<ApiCurrent>, response: Response<ApiCurrent>) {
                response.body()?.let {
                    currentWeather.set(it)
                    currentTemperature.set("${it.main?.temp?.toInt()}")
                    navigator?.onSuccess(it.weather?.get(0)?.main)
                }


            }

            override fun onFailure(call: Call<ApiCurrent>, t: Throwable) {
                navigator?.onError()
            }

        })
    }

}