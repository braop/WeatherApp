package com.example.weatherapp.viewModel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
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

    val currentTemperature = ObservableField<String>()
    val minimumTemperature = ObservableField<Int>()
    val maximumTemperature = ObservableField<Int>()
    val forecasts = ObservableField<List<ApiList>>()

    fun initiate() {
        getForecast()
    }

    private fun getForecast() {
        forecastClient.getForecast().enqueue(object : Callback<ApiForecast> {
            override fun onResponse(call: Call<ApiForecast>, response: Response<ApiForecast>) {
                response.body()?.let {
                    Log.d("Success", "onResponse: $it")
                }
            }

            override fun onFailure(call: Call<ApiForecast>, t: Throwable) {
                currentTemperature.set(t.message)
                Log.d("Error", "onFailure:${t.message} ")
            }

        })
    }

}