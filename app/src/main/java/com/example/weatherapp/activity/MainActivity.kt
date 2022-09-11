package com.example.weatherapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
                    Toast.makeText(this, "Location granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                AlertDialog.Builder(this)
                    .setTitle("Allow Location Permission")
                    .setMessage("This is required")
                    .setCancelable(false)
                    .setPositiveButton("Enable now") { _, _ ->
                        this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

}

interface MainInterface {
    fun onSuccess(status: String?)
    fun onError()
}