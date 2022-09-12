package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemSummaryBinding
import com.example.weatherapp.models.ForecastModel

class SummaryRecyclerviewAdapter :
    RecyclerView.Adapter<SummaryRecyclerviewAdapter.BindingHolder>() {

    var summaries: List<ForecastModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSummaryBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val forecast = summaries?.get(position)
        holder.binding.summary = forecast

        when (forecast?.status) {
            "Clouds" -> {
                holder.binding.icon.setImageResource(R.drawable.cloud)
            }
            "Rain" -> {
                holder.binding.icon.setImageResource(R.drawable.rain)
            }
            "Clear" -> {
                holder.binding.icon.setImageResource(R.drawable.sun)
            }
        }

        // change icons here
    }

    override fun getItemCount() = summaries?.size ?: 0

    class BindingHolder(var binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root)
}