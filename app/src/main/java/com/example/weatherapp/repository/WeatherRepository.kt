package com.example.weatherapp.repository

import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.entity.WeatherEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val db: AppDatabase
) {
    fun insertWeather(weatherEntity: WeatherEntity): Single<Long> {
        return db.weatherDao().insertWeather(weatherEntity)
    }

    fun selectWeather(): Single<WeatherEntity> {
        return db.weatherDao().selectWeather()
    }

    fun deleteAllWeather(): Single<Int> {
        return db.weatherDao().deleteAllWeather()
    }
}

