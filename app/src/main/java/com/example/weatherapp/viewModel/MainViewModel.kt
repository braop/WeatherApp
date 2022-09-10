package com.example.weatherapp.viewModel

import androidx.databinding.ObservableBoolean
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
    val currentTemp = ObservableField<String>()
    val currentStatus = ObservableField<String>()
    val minTemp = ObservableField<String>()
    val maxTemp = ObservableField<String>()
    val forecasts = ObservableField<List<ApiList>>()
    val loading = ObservableBoolean(false)
    var navigator: MainInterface? = null

    fun initiate(navigator: MainInterface) {
        this.navigator = navigator
        getForecast()
        getCurrentWeatherByLocation()
    }

    private fun getForecast() {
        loading.set(true)
        forecastClient.getForecast().enqueue(object : Callback<ApiForecast> {
            override fun onResponse(call: Call<ApiForecast>, response: Response<ApiForecast>) {
                response.body()?.let {
                    forecasts.set(it.list)
                }
                loading.set(false)
                // navigator?.onSuccess()
            }

            override fun onFailure(call: Call<ApiForecast>, t: Throwable) {
                loading.set(false)
                navigator?.onError()
            }

        })
    }

    private fun getCurrentWeatherByLocation() {
        loading.set(true)
        forecastClient.getCurrentWeather().enqueue(object : Callback<ApiCurrent> {
            override fun onResponse(call: Call<ApiCurrent>, response: Response<ApiCurrent>) {
                response.body()?.let {
                    currentWeather.set(it)
                    minTemp.set(it.main?.tempMin?.toInt().toString())
                    maxTemp.set(it.main?.tempMax?.toInt().toString())
                    currentTemp.set(it.main?.temp?.toInt().toString())
                    generateStatus(it.weather?.get(0)?.main)
                    navigator?.onSuccess(it.weather?.get(0)?.main)

                }
                loading.set(false)
            }

            override fun onFailure(call: Call<ApiCurrent>, t: Throwable) {
                loading.set(false)
                navigator?.onError()
            }

        })
    }

    private fun generateStatus(status: String?) {
        when (status) {
            "Clouds" -> {
                currentStatus.set("Cloudy")
            }
            "Rain" -> {
                currentStatus.set("Rainy")
            }
            "Sun" -> {
                currentStatus.set("Sunny")
            }
        }
    }

}