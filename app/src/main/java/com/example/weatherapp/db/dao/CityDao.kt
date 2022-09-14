package com.example.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.weatherapp.db.entity.CityEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(cityEntity: CityEntity): Single<Long>
}