package com.example.weatherapp.repository

import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.entity.ForecastEntity
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(
    private val db: AppDatabase
) {
    /*fun getForecast(): ForecastEntity {
        return db.forecastDao().getAllForecasts()
    }*/

    fun getForeCast(): ForecastEntity {
        val callable: Callable<ForecastEntity> = Callable { db.forecastDao().getAllForecasts() }
        val future: Future<ForecastEntity> = Executors.newSingleThreadExecutor().submit(callable)
        return future.get()
    }
}

