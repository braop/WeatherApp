package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemWeatherBinding
import com.example.weatherapp.models.ForecastModel
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class ForecastRecyclerviewAdapter :
    RecyclerView.Adapter<ForecastRecyclerviewAdapter.BindingHolder>() {

    var forecasts: List<ForecastModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val forecast = forecasts?.get(position)
        holder.binding.forecast = forecast

        holder.binding.temperature.text = forecast?.temp.toString()+ "\u00B0"

        val date = SimpleDateFormat("yyyy-MM-dd").parse(forecast?.dateText)
        val cal = Calendar.getInstance()
        cal.set(date.year, date.month, date.day)
        val dayName: String = DateFormatSymbols().weekdays[cal[Calendar.DAY_OF_WEEK]]

        Log.d("TAG", "onBindViewHolder: ${dayName}")
        holder.binding.day.text = dayName
        when (forecast?.status) {
            "Clouds" -> {
                holder.binding.icon.setImageResource(R.drawable.icn_partly_sunny)
            }
            "Rain" -> {
                holder.binding.icon.setImageResource(R.drawable.icn_rain)
            }
            "Clear" -> {
                holder.binding.icon.setImageResource(R.drawable.icn_clear)
            }
        }

        // change icons here
    }

    override fun getItemCount() = forecasts?.size ?: 0

    class BindingHolder(var binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)
}