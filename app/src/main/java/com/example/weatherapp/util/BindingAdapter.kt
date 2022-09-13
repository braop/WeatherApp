package com.example.weatherapp.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.adapter.SummaryRecyclerviewAdapter
import com.example.weatherapp.api.response.ApiList
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
    @BindingAdapter("summary")
    @JvmStatic
    fun RecyclerView.setSummary(apiList: ObservableField<List<ApiList>>) {

        adapter?.apply {
            this as SummaryRecyclerviewAdapter
            this.summaries = apiList.get()
            notifyDataSetChanged()
        }

    }
}