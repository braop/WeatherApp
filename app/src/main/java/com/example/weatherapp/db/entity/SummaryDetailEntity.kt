package com.example.weatherapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summary_detail")
data class SummaryDetailEntity constructor(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var status: String?,
    var dateText: String?,
    var temp: Int?
)