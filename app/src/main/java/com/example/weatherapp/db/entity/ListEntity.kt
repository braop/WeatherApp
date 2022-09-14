package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "list")
data class ListEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var dt: Int?,
    var visibility: Int?,
    var pop: Double?,
    @SerializedName("dt_txt")
    var dtTxt: String?
)