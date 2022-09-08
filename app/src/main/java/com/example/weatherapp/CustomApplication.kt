package com.example.weatherapp

import android.app.Application

class CustomApplication : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun getComponent(): AppComponent {
        return appComponent
    }

}
