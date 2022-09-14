package com.example.weatherapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.weatherapp.db.entity.MainEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMain(mainEntity: MainEntity): Single<Long>
}