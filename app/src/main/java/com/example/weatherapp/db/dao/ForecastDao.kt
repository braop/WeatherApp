package com.example.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.db.entity.ForecastEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addForecast(forecastEntity: ForecastEntity): Long

   @Query("SELECT *From forecast")
    fun getAllForecasts(): ForecastEntity


}