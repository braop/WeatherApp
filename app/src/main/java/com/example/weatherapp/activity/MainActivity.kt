package com.example.weatherapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewModel.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainInterface {

    @Inject
    lateinit var viewModel: MainViewModel

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as CustomApplication).getComponent().inject(this)

        binding.viewModel = viewModel

        viewModel.initiate(this)

        binding.forecastRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = ForecastRecyclerviewAdapter()
        }

    }

    override fun onSuccess(status: String?) {

        when (status) {
            "Clouds" -> {
                binding.headerImage.setImageResource(R.drawable.forest_cloudy)
                binding.mainLayout.setBackgroundColor(resources.getColor(R.color.color_cloudy))

            }
            "Rain" -> {
                binding.headerImage.setImageResource(R.drawable.forest_rainy)
                binding.mainLayout.setBackgroundColor(resources.getColor(R.color.color_rainy))
            }
            "Sun" -> {
                binding.headerImage.setImageResource(R.drawable.forest_sunny)
                binding.mainLayout.setBackgroundColor(resources.getColor(R.color.color_sunny))
            }
        }
    }

    override fun onError() {
        Toast.makeText(this, "Error, Please try again", Toast.LENGTH_SHORT).show()
    }

}

interface MainInterface {
    fun onSuccess(status: String?)
    fun onError()
}