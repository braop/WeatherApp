package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.db.dao.ForecastDao
import com.example.weatherapp.db.entity.ForecastEntity

@Database(entities = [ForecastEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}