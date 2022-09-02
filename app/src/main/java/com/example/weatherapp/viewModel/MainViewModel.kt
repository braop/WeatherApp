package com.example.weatherapp.viewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val currentTemparature = ObservableField<String>()
    val minimumTemparature = ObservableField<Int>()
    val maximumTemperature = ObservableField<Int>()

    fun initiate() {
        currentTemparature.set("26")
    }

}