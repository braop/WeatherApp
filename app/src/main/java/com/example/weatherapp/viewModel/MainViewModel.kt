package com.example.weatherapp.viewModel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.activity.MainInterface
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.api.response.ApiList
import com.example.weatherapp.clients.ForecastClient
import com.example.weatherapp.clients.WeatherClient
import com.example.weatherapp.models.ForecastModel
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
    val forecasts = ObservableField<List<ForecastModel>>()
    val listOfForecasts = arrayListOf<ForecastModel>()
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
                                minTemp.set(it.list?.get(0)?.main?.tempMin?.toInt().toString())
                                maxTemp.set(it.list?.get(0)?.main?.tempMax?.toInt().toString())

                                it.list?.forEach { apiList ->

                                    if (listOfForecasts.isEmpty()) {

                                        listOfForecasts.add(
                                            ForecastModel(
                                                apiList.dtTxt?.trim()?.substring(0, 10),
                                                apiList.main?.temp?.toInt(),
                                                apiList.weather?.get(0)?.main
                                            )
                                        )
                                    } else {
                                        var boo = 0;
                                        listOfForecasts.forEach {
                                            if (it.dateText.equals(
                                                    apiList.dtTxt?.trim()?.substring(0, 10)
                                                )
                                            ) {
                                                boo = 1
                                            }
                                        }
                                        if (boo == 0) {
                                            listOfForecasts.add(
                                                ForecastModel(
                                                    apiList.dtTxt?.trim()?.substring(0, 10),
                                                    apiList.main?.temp?.toInt(),
                                                    apiList.weather?.get(0)?.main
                                                )
                                            )
                                        }
                                    }

                                }

                            }
                            Log.d("TAG", "onResponse: $listOfForecasts")
                            forecasts.set(listOfForecasts)
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