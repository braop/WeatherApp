package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var main: String?,
    var description: String?,
    var minTemp: Int?,
    var maxTemp: Int?,
    var temp: Int?,
    var feelsLike: Int?,
    var name: String?
)