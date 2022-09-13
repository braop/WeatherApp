package com.example.weatherapp.viewModel

import android.annotation.SuppressLint
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
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val forecastClient: ForecastClient,
    private val weatherClient: WeatherClient
) : ViewModel() {

    val currentWeather = ObservableField<ApiCurrent>()
    val currentTemp = ObservableField<Int>()
    val currentStatus = ObservableField<String>()
    val feelsLike = ObservableField<String>()
    val minTemp = ObservableField<Int>()
    val maxTemp = ObservableField<Int>()
    val forecasts = ObservableField<List<ForecastModel>>()
    val summaryDetails = ObservableField<ApiForecast>()
    val summary = ObservableField<List<ApiList>>()
    val listOfForecasts = arrayListOf<ForecastModel>()
    val loading = ObservableBoolean(false)
    var navigator: MainInterface? = null
    var timeInfoUpdate = ObservableField<String>()

    val current: LocalDateTime? = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

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

                                summaryDetails.set(it)
                                minTemp.set(it.list?.get(0)?.main?.tempMin?.toInt())
                                maxTemp.set(it.list?.get(0)?.main?.tempMax?.toInt())
                                summary.set(it.list)

                                it.list?.forEach { apiList ->

                                    if (listOfForecasts.isEmpty()) {

                                        listOfForecasts.add(
                                            ForecastModel(
                                                apiList.dtTxt?.trim()?.substring(0, 10),
                                                getDayName(apiList.dtTxt?.trim()?.substring(0, 10)),
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
                                                    getDayName(
                                                        apiList.dtTxt?.trim()?.substring(0, 10)
                                                    ),
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

    @SuppressLint("SimpleDateFormat")
    fun getDayName(dateText: String?): String {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateText)
        val cal = Calendar.getInstance()
        cal.set(date.year, date.month, date.day)
        return DateFormatSymbols().weekdays[cal[Calendar.DAY_OF_WEEK]]
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
                                minTemp.set(it.main?.tempMin?.toInt())
                                maxTemp.set(it.main?.tempMax?.toInt())
                                currentTemp.set(it.main?.temp?.toInt())
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

        val formatted = current?.format(formatter)

        when (status) {
            "Clouds" -> {
                currentStatus.set("Cloudy")
                timeInfoUpdate.set(formatted.toString() + ", Mostly Cloudy")
            }
            "Rain" -> {
                currentStatus.set("Rainy")
                timeInfoUpdate.set(formatted.toString() + ", Mostly Rainy")
            }
            "Clear" -> {
                currentStatus.set("Sunny")
                timeInfoUpdate.set(formatted.toString() + ", Mostly Clear Sky")
            }
        }
    }

}