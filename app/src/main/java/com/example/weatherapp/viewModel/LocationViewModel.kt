package com.example.weatherapp.viewModel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.activity.LocationNavigator
import com.example.weatherapp.api.response.ApiCity
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.clients.ForecastClient
import com.example.weatherapp.clients.WeatherClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationViewModel @Inject constructor(
    private val weatherClient: WeatherClient,
    private val forecastClient: ForecastClient,
    val context: Context
) : ViewModel() {

    val latObservable = ObservableField<Double>()
    val longObservable = ObservableField<Double>()
    val city = ObservableField<ApiCity>()
    val loading = ObservableBoolean(false)
    val online = ObservableField(false)

    var navigator: LocationNavigator? = null

    fun initiate(navigator: LocationNavigator, latitude: Double?, longitude: Double?) {
        this.navigator = navigator
        getForecast(latitude, longitude)
    }

    private fun getForecast(latitude: Double?, longitude: Double?) {
        if ((context as CustomApplication).isNetworkConnected(context)) {
            loading.set(true)
            online.set(true)
            latitude?.let { lat ->
                longitude?.let { long ->
                    weatherClient.getWeather(lat, long)
                        .enqueue(object : Callback<ApiCurrent> {
                            override fun onResponse(
                                call: Call<ApiCurrent>,
                                response: Response<ApiCurrent>
                            ) {
                                response.body()?.let {
                                    latObservable.set(it.coord?.lat)
                                    longObservable.set(it.coord?.lon)

                                    getCityInfo(it.coord?.lat, it.coord?.lon)
                                }
                                loading.set(false)
                                navigator?.onSuccess()
                            }

                            override fun onFailure(call: Call<ApiCurrent>, t: Throwable) {
                                loading.set(false)
                                navigator?.onError()
                            }

                        })
                }
            }
        } else {
            // no internet connection
            navigator?.onNoInternet()
        }

    }

    private fun getCityInfo(latitude: Double?, longitude: Double?) {
        if ((context as CustomApplication).isNetworkConnected(context)) {
            loading.set(true)
            online.set(true)
            latitude?.let { lat ->
                longitude?.let { long ->
                    forecastClient.getForecast(lat, long)
                        .enqueue(object : Callback<ApiForecast> {
                            override fun onResponse(
                                call: Call<ApiForecast>,
                                response: Response<ApiForecast>
                            ) {
                                response.body()?.let {
                                    city.set(it.city)
                                }
                                loading.set(false)
                            }

                            override fun onFailure(call: Call<ApiForecast>, t: Throwable) {
                                loading.set(false)
                                navigator?.onError()
                            }

                        })
                }
            }
        } else {
            // no internet connection
            navigator?.onNoInternet()
        }

    }

}