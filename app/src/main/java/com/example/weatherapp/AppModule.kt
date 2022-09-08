package com.example.weatherapp

import android.app.Application
import android.content.Context
import com.example.weatherapp.api.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideApiService(): ApiService.IApiService {
        return ApiService(application).service
    }


}