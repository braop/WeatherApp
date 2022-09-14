package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "main")
data class MainEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var temp: Double?,
    @SerializedName("feels_like")
    var feelsLike: Double?,
    @SerializedName("temp_min")
    var tempMin: Double?,
    @SerializedName("temp_max")
    var tempMax: Double?,
    var pressure: Int?,
    @SerializedName("sea_level")
    var seaLevel: Int?,
    @SerializedName("grnd_level")
    var grndLevel: Int?,
    var humidity: Int?,
    @SerializedName("temp_kf")
    var tempKf: Double?
)