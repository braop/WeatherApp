package com.example.weatherapp.repository

import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.entity.ForecastEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(
    private val db: AppDatabase
) {
    fun insertForecast(forecastEntity: ForecastEntity): Single<Long> {
        return db.forecastDao().insertForecast(forecastEntity)
    }

    fun selectForeCast(): Single<ForecastEntity> {
        return db.forecastDao().selectForecast()
    }
}

