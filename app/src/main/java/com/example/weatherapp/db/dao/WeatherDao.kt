package com.example.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.db.entity.WeatherEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherEntity: WeatherEntity): Single<Long>

    @Query("SELECT *From weather")
    fun selectWeather(): Single<WeatherEntity>

    @Query("SELECT COUNT(*) From weather")
    fun selectCountWeather(): Single<Long>

    @Query("DELETE FROM weather")
    fun deleteAllWeather(): Single<Int>
}