package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.db.dao.ForecastDao
import com.example.weatherapp.db.dao.WeatherDao
import com.example.weatherapp.db.entity.*

@Database(
    entities = [ForecastEntity::class, MainEntity::class, ListEntity::class, CityEntity::class, WeatherEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
    abstract fun weatherDao(): WeatherDao
}