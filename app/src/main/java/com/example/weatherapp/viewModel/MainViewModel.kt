package com.example.weatherapp.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.activity.MainInterface
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.clients.ForecastClient
import com.example.weatherapp.clients.WeatherClient
import com.example.weatherapp.db.entity.DetailedForecastEntity
import com.example.weatherapp.db.entity.ForecastEntity
import com.example.weatherapp.db.entity.WeatherEntity
import com.example.weatherapp.models.DetailedForecastModel
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.repository.DetailedForecastRepository
import com.example.weatherapp.repository.ForecastRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.Constants
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
    private val weatherClient: WeatherClient,
    private val forecastRepository: ForecastRepository,
    private val weatherRepository: WeatherRepository,
    private val detailedForecastRepository: DetailedForecastRepository,
    val context: Context
) : ViewModel() {

    val currentWeather = ObservableField<ApiCurrent>()
    val currentTemp = ObservableField<Int>()
    val currentStatus = ObservableField<String>()
    val feelsLike = ObservableField<String>()
    val minTemp = ObservableField<Int>()
    val maxTemp = ObservableField<Int>()

    val city = ObservableField<String>()

    val detailedForecasts = ObservableField<List<DetailedForecastModel>>()
    val listOfDetailedForecasts = arrayListOf<DetailedForecastModel>()

    val summarisedforecasts = ObservableField<List<ForecastModel>>()
    val listOfSummariedForecasts = arrayListOf<ForecastModel>()

    val loading = ObservableBoolean(false)
    val online = ObservableField(false)
    val noPermission = ObservableField(false)

    var navigator: MainInterface? = null
    var timeInfoUpdate = ObservableField<String>()

    private val current: LocalDateTime? = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

    @SuppressLint("SimpleDateFormat")
    private val createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    val lastUpdatedAt = ObservableField<String>()

    fun initiate(navigator: MainInterface) {
        this.navigator = navigator
    }

    fun getDetailedForecastFromApi(latitude: Double?, longitude: Double?, isSearch: Boolean) {

        if ((context as CustomApplication).isNetworkConnected(context)) {

            listOfDetailedForecasts.clear()
            listOfSummariedForecasts.clear()
            summarisedforecasts.set(null)
            detailedForecasts.set(null)

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

                                    minTemp.set(it.list?.get(0)?.main?.tempMin?.toInt())
                                    maxTemp.set(it.list?.get(0)?.main?.tempMax?.toInt())
                                    city.set(it.city?.name)

                                    it.list?.forEach { apiList ->

                                        //detailed forecast
                                        listOfDetailedForecasts.add(
                                            DetailedForecastModel(
                                                null,
                                                apiList.weather?.get(0)?.main,
                                                apiList.dtTxt,
                                                apiList.main?.temp?.toInt()
                                            )
                                        )

                                        //summarized forecast
                                        if (listOfSummariedForecasts.isEmpty()) {
                                            listOfSummariedForecasts.add(
                                                ForecastModel(
                                                    apiList.dtTxt?.trim()?.substring(0, 10),
                                                    getDayName(
                                                        apiList.dtTxt?.trim()?.substring(0, 10)
                                                    ),
                                                    apiList.main?.temp?.toInt(),
                                                    apiList.weather?.get(0)?.main
                                                )
                                            )

                                        } else {
                                            var count = 0
                                            listOfSummariedForecasts.forEach { forecastModel ->
                                                if (forecastModel.dateText.equals(
                                                        apiList.dtTxt?.trim()?.substring(0, 10)
                                                    )
                                                ) {
                                                    count = 1
                                                }
                                            }
                                            if (count == 0) {
                                                listOfSummariedForecasts.add(
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

                                summarisedforecasts.set(listOfSummariedForecasts)
                                detailedForecasts.set(listOfDetailedForecasts)

                                navigator?.onForecastSuccess(
                                    summarisedforecasts.get(),
                                    detailedForecasts.get(),
                                    isSearch
                                )
                                loading.set(false)
                            }

                            override fun onFailure(call: Call<ApiForecast>, t: Throwable) {
                                loading.set(false)
                                navigator?.onError(t)
                            }

                        })
                }
            }
        } else {
            // no internet connection
            online.set(false)
            selectForecastLocalDB()
            selectDetailedForecastsLocalDB()
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun getDayName(dateText: String?): String {
        val date: Date? = SimpleDateFormat("yyyy-MM-dd").parse(dateText)
        val cal = Calendar.getInstance()
        cal.set(date!!.year, date.month, date.day)
        return DateFormatSymbols().weekdays[cal[Calendar.DAY_OF_WEEK]]
    }

    private fun insertForecastToDB(forecasts: ForecastEntity) {
        forecastRepository.insertForecast(
            forecasts
        ).subscribe(
            {
                navigator?.onInsertForecastSuccess()
            },
            {
                navigator?.onInsertForecastError(it)
            }
        )

    }

    private fun insertDetailedForecastToDB(detailedForecast: DetailedForecastEntity) {
        detailedForecastRepository.insertDetailedForecast(
            detailedForecast
        ).subscribe(
            {
                navigator?.onInsertDetailedForecastSuccess()
            },
            {
                navigator?.onInsertDetailedForecastError()
            }
        )

    }

    fun selectForecastLocalDB() {
        loading.set(true)
        forecastRepository.selectForeCast().subscribe(
            {
                it.forEach { forecastEntity ->
                    listOfSummariedForecasts.add(
                        ForecastModel(
                            forecastEntity.dateText,
                            forecastEntity.dayName,
                            forecastEntity.temp,
                            forecastEntity.status
                        )
                    )
                }
                summarisedforecasts.set(listOfSummariedForecasts)

                loading.set(false)
                navigator?.onSelectForecastSuccess(it)
            },
            {
                // Tell user to check internet connection
                loading.set(false)
                navigator?.onError(it)
            }
        )
    }

    fun selectDetailedForecastsLocalDB() {
        loading.set(true)
        detailedForecastRepository.selectDetailedForeCast().subscribe(
            {
                it.forEach { detailedForecastEntity ->
                    listOfDetailedForecasts.add(
                        DetailedForecastModel(
                            detailedForecastEntity.id,
                            detailedForecastEntity.status,
                            detailedForecastEntity.dateText,
                            detailedForecastEntity.temp
                        )
                    )
                }
                detailedForecasts.set(listOfDetailedForecasts)
                loading.set(false)
            },
            {
                // Tell user to check internet connection
                loading.set(false)
                navigator?.onError(it)
            }
        )
    }

    fun deleteAllForecastFromDB(forecasts: List<ForecastModel>?) {
        forecastRepository.deleteAllForeCast().subscribe(
            {
                forecasts?.forEach {
                    insertForecastToDB(
                        ForecastEntity(
                            null,
                            it.dateText,
                            it.dayName,
                            it.temp,
                            it.status
                        )
                    )
                }

            },
            {
                navigator?.onError(it)
            }
        )
    }

    fun deleteDetailedForecastsFromDB(detailedForecasts: List<DetailedForecastModel>?) {
        detailedForecastRepository.deleteAllDetailedForeCast().subscribe(
            {
                detailedForecasts?.forEach {
                    insertDetailedForecastToDB(
                        DetailedForecastEntity(
                            null,
                            it.weatherStatus,
                            it.dtTxt,
                            it.temp
                        )
                    )
                }

            },
            {
                navigator?.onError(it)
            }
        )
    }

    fun deleteWeatherFromDB(currentWeather: ApiCurrent?) {
        weatherRepository.deleteAllWeather().subscribe(
            {
                insertWeatherToDB(currentWeather)
            },
            {

            }
        )
    }

    fun insertWeatherToDB(currentWeather: ApiCurrent?) {
        weatherRepository.insertWeather(
            WeatherEntity(
                null,
                currentWeather?.weather?.get(0)?.main,
                currentWeather?.weather?.get(0)?.description,
                currentWeather?.main?.tempMax?.toInt(),
                currentWeather?.main?.tempMin?.toInt(),
                currentWeather?.main?.temp?.toInt(),
                currentWeather?.main?.feelsLike?.toInt(),
                currentWeather?.name,
                createdAt.format(Date())
            )
        ).subscribe(
            {
            },
            {

            }
        )
    }

    fun getWeatherApi(
        latitude: Double?,
        longitude: Double?,
        isSearch: Boolean
    ) {
        if ((context as CustomApplication).isNetworkConnected(context)) {
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
                                    generateStatus(
                                        it.weather?.get(0)?.main,
                                        it.weather?.get(0)?.description
                                    )


                                    navigator?.onSuccess(it.weather?.get(0)?.main)
                                }
                                loading.set(false)
                                navigator?.onGetApiWeatherSuccess(currentWeather.get(), isSearch)
                            }

                            override fun onFailure(call: Call<ApiCurrent>, t: Throwable) {
                                loading.set(false)
                                navigator?.onError(t)
                            }

                        })
                }
            }

        } else {
            // no internet connection
            online.set(false)
            selectWeatherFromDB()
        }
    }

    fun selectWeatherFromDB() {
        loading.set(true)
        weatherRepository.selectWeather().subscribe(
            {
                currentStatus.set(it.main)
                currentTemp.set(it.temp)
                minTemp.set(it.minTemp)
                maxTemp.set(it.maxTemp)
                city.set(it.name)
                generateStatus(it.main, it.description)
                lastUpdatedAt.set(
                    "Last updated at: " + createdAt.parse(it.createdAt).toString().trim()
                        .substring(0, 19)
                )
                loading.set(false)
                navigator?.onSuccess(it.main)
            },
            {
                loading.set(false)
            }
        )
    }

    fun setPermissionStatus(boolean: Boolean) {
        noPermission.set(boolean)
    }

    fun destroy() {
        currentWeather.set(null)
        currentTemp.set(null)
        currentStatus.set(null)
        feelsLike.set(null)
        minTemp.set(null)
        maxTemp.set(null)
        summarisedforecasts.set(null)
        online.set(false)
        city.set(null)
        noPermission.set(false)
        navigator = null
    }

    private fun generateStatus(status: String?, description: String?) {

        val formatted = current?.format(formatter)

        when (status) {
            Constants.CLOUDS -> {
                currentStatus.set(Constants.CLOUDY)
                timeInfoUpdate.set(formatted.toString() + ", " + description)
            }
            Constants.RAIN -> {
                currentStatus.set(Constants.RAINY)
                timeInfoUpdate.set(formatted.toString() + ", " + description)
            }
            Constants.CLEAR -> {
                currentStatus.set(Constants.SUNNY)
                timeInfoUpdate.set(formatted.toString() + ", " + description)
            }
        }
    }

}