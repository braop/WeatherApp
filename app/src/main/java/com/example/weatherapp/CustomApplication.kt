package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

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

    //check if device is connected online
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        capabilities.also {
            if (it != null) {
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                }
            }
        }
        return false
    }

}
