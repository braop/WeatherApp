package com.example.weatherapp

import com.example.weatherapp.activity.LocationActivity
import com.example.weatherapp.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: LocationActivity)

}