package com.example.weatherapp.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val currentTemperature = ObservableField<String>()
    val minimumTemperature = ObservableField<Int>()
    val maximumTemperature = ObservableField<Int>()

    fun initiate() {
        currentTemperature.set("26")
    }

}