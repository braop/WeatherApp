package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
)