package com.example.weatherapp.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.activity.MainInterface
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.api.response.ApiList
import com.example.weatherapp.clients.ForecastClient
import com.example.weatherapp.clients.WeatherClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val forecastClient: ForecastClient,
    private val weatherClient: WeatherClient
) : ViewModel() {

    val currentWeather = ObservableField<ApiCurrent>()
    val currentTemp = ObservableField<String>()
    val currentStatus = ObservableField<String>()
    val feelsLike = ObservableField<String>()
    val minTemp = ObservableField<String>()
    val maxTemp = ObservableField<String>()
    val forecasts = ObservableField<List<ApiList>>()
    val loading = ObservableBoolean(false)
    var navigator: MainInterface? = null

    fun initiate(navigator: MainInterface) {
        this.navigator = navigator
    }

    fun getForecast(latitude: Double?, longitude: Double?) {
        loading.set(true)
        latitude?.let { lat ->
            longitude?.let { long ->
                forecastClient.getForecast(lat, long)
                    .enqueue(object : Callback<ApiForecast> {
                        override fun onResponse(
                            call: Call<ApiForecast>,
                            response: Response<ApiForecast>
                        ) {
                            response.body()?.let {
                                forecasts.set(it.list)
                                minTemp.set(it.list?.get(0)?.main?.tempMin?.toInt().toString())
                                maxTemp.set(it.list?.get(0)?.main?.tempMax?.toInt().toString())
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
        }
    }

    fun getWeather(
        latitude: Double?,
        longitude: Double?
    ) {
        loading.set(true)
        latitude?.let { lat ->
            longitude?.let { long ->
                weatherClient.getWeather(lat, long)
                    .enqueue(object : Callback<ApiCurrent> {
                        override fun onResponse(
                            call: Call<ApiCurrent>,
                            response: Response<ApiCurrent>
                        ) {
                            response.body()?.let {
                                currentWeather.set(it)
                                minTemp.set(it.main?.tempMin?.toInt().toString())
                                maxTemp.set(it.main?.tempMax?.toInt().toString())
                                currentTemp.set(it.main?.temp?.toInt().toString())
                                feelsLike.set(it.main?.feelsLike?.toInt().toString())
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
        }
    }

    fun destroy() {
        currentWeather.set(null)
        currentTemp.set(null)
        currentStatus.set(null)
        feelsLike.set(null)
        minTemp.set(null)
        maxTemp.set(null)
        forecasts.set(null)
    }

    private fun generateStatus(status: String?) {
        when (status) {
            "Clouds" -> {
                currentStatus.set("Cloudy")
            }
            "Rain" -> {
                currentStatus.set("Rainy")
            }
            "Clear" -> {
                currentStatus.set("Sunny")
            }
        }
    }

}