package com.example.weatherapp.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.SummarisedForecastRecyclerviewAdapter
import com.example.weatherapp.adapter.DetailedForecastRecyclerviewAdapter
import com.example.weatherapp.models.DetailedForecastModel
import com.example.weatherapp.models.ForecastModel

object BindingAdapter {

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("forecast")
    @JvmStatic
    fun RecyclerView.setForecast(summarisedForecasts: ObservableField<List<ForecastModel>>) {

        adapter?.apply {
            this as SummarisedForecastRecyclerviewAdapter
            this.forecasts = summarisedForecasts.get()
            notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("detailed_forecast")
    @JvmStatic
    fun RecyclerView.setSummary(detailedForecasts: ObservableField<List<DetailedForecastModel>>) {

        adapter?.apply {
            this as DetailedForecastRecyclerviewAdapter
            this.detailedForecasts = detailedForecasts.get()
            notifyDataSetChanged()
        }

    }
}