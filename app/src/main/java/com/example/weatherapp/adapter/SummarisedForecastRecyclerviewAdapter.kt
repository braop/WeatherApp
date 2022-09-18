package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemWeatherBinding
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.util.Constants


class SummarisedForecastRecyclerviewAdapter :
    RecyclerView.Adapter<SummarisedForecastRecyclerviewAdapter.BindingHolder>() {

    var forecasts: List<ForecastModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val forecast = forecasts?.get(position)
        holder.binding.forecast = forecast

        when (forecast?.status) {
            Constants.CLOUDS -> {
                holder.binding.icon.setImageResource(R.drawable.cloud)
            }
            Constants.RAIN -> {
                holder.binding.icon.setImageResource(R.drawable.rain)
            }
            Constants.CLEAR -> {
                holder.binding.icon.setImageResource(R.drawable.sun)
            }
        }
    }

    override fun getItemCount() = forecasts?.size ?: 0

    class BindingHolder(var binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)
}