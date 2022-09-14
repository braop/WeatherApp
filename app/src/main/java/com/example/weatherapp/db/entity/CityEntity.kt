package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String?,
    var country: String?,
    var population: Int?,
    var timezone: Int?,
    var sunrise: Int?,
    var sunset: Int?
)