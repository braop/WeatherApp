package com.example.weatherapp.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.models.ForecastModel

object BindingAdapter {

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("forecasts")
    @JvmStatic
    fun RecyclerView.setProducts(apiList: ObservableField<List<ForecastModel>>) {

        adapter?.apply {
            this as ForecastRecyclerviewAdapter
            this.forecasts = apiList.get()
            notifyDataSetChanged()
        }

    }
}