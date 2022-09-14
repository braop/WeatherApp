package com.example.weatherapp.repository

import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.entity.DetailedForecastEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailedForecastRepository @Inject constructor(
    private val db: AppDatabase
) {
    fun insertDetailedForecast(detailedForecastEntity: DetailedForecastEntity): Single<Long> {
        return db.detailedForecastDao().insertDetailedForecast(detailedForecastEntity)
    }

    fun selectDetailedForeCast(): Single<List<DetailedForecastEntity>> {
        return db.detailedForecastDao().selectDetailedForecast()
    }

    fun deleteAllDetailedForeCast(): Single<Int> {
        return db.detailedForecastDao().deleteAllDetailedForecast()
    }
}

