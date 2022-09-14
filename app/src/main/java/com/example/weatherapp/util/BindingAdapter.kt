package com.example.weatherapp.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.adapter.SummaryRecyclerviewAdapter
import com.example.weatherapp.models.DetailedForecastModel
import com.example.weatherapp.models.ForecastModel

object BindingAdapter {

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("forecast")
    @JvmStatic
    fun RecyclerView.setForecast(apiList: ObservableField<List<ForecastModel>>) {

        adapter?.apply {
            this as ForecastRecyclerviewAdapter
            this.forecasts = apiList.get()
            notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("detailed_forecast")
    @JvmStatic
    fun RecyclerView.setSummary(detailedForecasts: ObservableField<List<DetailedForecastModel>>) {

        adapter?.apply {
            this as SummaryRecyclerviewAdapter
            this.detailedForecasts = detailedForecasts.get()
            notifyDataSetChanged()
        }

    }
}