package com.example.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.db.entity.DetailedForecastEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface DetailedForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetailedForecast(detailedForecastEntity: DetailedForecastEntity): Single<Long>

    @Query("SELECT *From detailed_forecast")
    fun selectDetailedForecast(): Single<List<DetailedForecastEntity>>

    @Query("SELECT COUNT(*) From detailed_forecast")
    fun selectCountDetailedForecast(): Single<Long>

    @Query("DELETE FROM detailed_forecast")
    fun deleteAllDetailedForecast(): Single<Int>
}