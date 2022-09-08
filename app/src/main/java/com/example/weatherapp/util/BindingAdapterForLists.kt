package com.example.weatherapp.util

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.api.response.ApiList

object BindingAdapterForLists {

    @SuppressLint("NotifyDataSetChanged")
    @BindingAdapter("forecasts")
    @JvmStatic
    fun RecyclerView.setProducts(apiList: ObservableField<List<ApiList>>) {

        adapter?.apply {
            this as ForecastRecyclerviewAdapter
            this.forecasts = apiList.get()
            notifyDataSetChanged()
        }

    }
}