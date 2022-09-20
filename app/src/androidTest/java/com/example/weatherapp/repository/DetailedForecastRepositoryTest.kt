package com.example.weatherapp.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.db.AppDatabase
import com.example.weatherapp.db.entity.DetailedForecastEntity
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailedForecastRepositoryTest : TestCase() {

    private lateinit var detailedForecastRepository: DetailedForecastRepository

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()

        detailedForecastRepository = DetailedForecastRepository(db)

    }

    @Test
    fun testDetailedForecastRepository() {
        val detailedForecast = DetailedForecastEntity(null, "Cloudy", "Tuesday", 20)
        detailedForecastRepository.insertDetailedForecast(detailedForecast)
        val detailForecasts = detailedForecastRepository.selectDetailedForeCast()
        assertThat(detailForecasts.contains(detailedForecast))
    }
}