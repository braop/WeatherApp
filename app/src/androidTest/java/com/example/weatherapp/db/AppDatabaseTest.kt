package com.example.weatherapp.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.db.dao.DetailedForecastDao
import com.example.weatherapp.db.dao.ForecastDao
import com.example.weatherapp.db.dao.WeatherDao
import com.example.weatherapp.db.entity.DetailedForecastEntity
import com.example.weatherapp.db.entity.ForecastEntity
import com.example.weatherapp.db.entity.WeatherEntity
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {

    private lateinit var db: AppDatabase
    private lateinit var weatherDao: WeatherDao
    private lateinit var forecastDao: ForecastDao
    private lateinit var detailedForecastDao: DetailedForecastDao

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        weatherDao = db.weatherDao()
        forecastDao = db.forecastDao()
        detailedForecastDao = db.detailedForecastDao()
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun writeReadWeather() {
        val weather = WeatherEntity(null, "Cloudy", "broken clouds", 20, 22, 22, 99, "Kampala", "")
        weatherDao.insertWeather(weather)
        val weathers = weatherDao.selectWeather()
        assertThat(weathers.contains(weather))
    }

    @Test
    fun writeReadForecasts(){
        val forecast = ForecastEntity(null,"","Tuesday",20,"Clouds")
        forecastDao.insertForecast(forecast)
        val forecasts = forecastDao.selectForecast()
        assertThat(forecasts.contains(forecast))
    }

    @Test
    fun writeReadDetailedForecasts(){
        val detailedForecast = DetailedForecastEntity(null,"Cloudy","Tuesday",20)
        detailedForecastDao.insertDetailedForecast(detailedForecast)
        val detailedForecasts = detailedForecastDao.selectDetailedForecast()
        assertThat(detailedForecasts.contains(detailedForecast))
    }

}