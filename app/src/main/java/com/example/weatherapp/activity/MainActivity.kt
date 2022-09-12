package com.example.weatherapp.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ForecastRecyclerviewAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainInterface {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as CustomApplication).getComponent().inject(this)
        binding.viewModel = viewModel
        viewModel.initiate(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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
            "Clear" -> {
                binding.headerImage.setImageResource(R.drawable.forest_sunny)
                binding.mainLayout.setBackgroundColor(resources.getColor(R.color.color_sunny))
            }
        }
    }

    override fun onError() {
        Toast.makeText(this, "Error, Please try again", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation()
                } else {
                    // permission not granted
                }
            }
            else -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastKnownLocation()
        } else {
            askLocationPermission()
        }
    }

    private fun getLastKnownLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                viewModel.getWeather(location.latitude, location.longitude)
                viewModel.getForecast(location.latitude, location.longitude)
            } else {
                Toast.makeText(this, "No known location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE
                )
            } else {

                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE
                )

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1000
    }

}

interface MainInterface {
    fun onSuccess(status: String?)
    fun onError()
}