package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailed_forecast")
data class DetailedForecastEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var status: String?,
    var dateText: String?,
    var temp: Int?
)