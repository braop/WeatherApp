package com.example.weatherapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemSummaryBinding
import com.example.weatherapp.models.DetailedForecastModel
import com.example.weatherapp.util.Constants
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class SummaryRecyclerviewAdapter :
    RecyclerView.Adapter<SummaryRecyclerviewAdapter.BindingHolder>() {

    var detailedForecasts: List<DetailedForecastModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSummaryBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }


    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val forecast = detailedForecasts?.get(position)
        holder.binding.forecastDetails = forecast

        holder.binding.dateTime.text = getDayName(forecast?.dtTxt?.trim()?.substring(0, 10))
        holder.binding.time.text = forecast?.dtTxt?.trim()?.substring(11, 16)
        //2202-02-20 00:00:00

        when (forecast?.weatherStatus) {
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

    override fun getItemCount() = detailedForecasts?.size ?: 0

    @SuppressLint("SimpleDateFormat")
    fun getDayName(dateText: String?): String {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateText)
        val cal = Calendar.getInstance()
        cal.set(date.year, date.month, date.day)
        return DateFormatSymbols().weekdays[cal[Calendar.DAY_OF_WEEK]]
    }

    class BindingHolder(var binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root)
}