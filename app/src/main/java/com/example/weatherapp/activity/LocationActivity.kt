package com.example.weatherapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityLocationBinding
import com.example.weatherapp.viewModel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class LocationActivity : AppCompatActivity(), LocationNavigator, OnMapReadyCallback {

    @Inject
    lateinit var viewModel: LocationViewModel

    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    private val binding: ActivityLocationBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_location)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as CustomApplication).getComponent().inject(this)
        binding.viewModel = viewModel

        val bundle = intent.extras

        viewModel.initiate(this, bundle?.getDouble("latitude"), bundle?.getDouble("longitude"))
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val coordinates = LatLng(
            viewModel.latObservable.get()!!,
            viewModel.longObservable.get()!!
        )
        marker = googleMap.addMarker(
            MarkerOptions()
                .position(coordinates)
                .title("You are here")
                .draggable(true)
        )

        val cameraPosition = CameraPosition.Builder()
            .target(coordinates)
            .zoom(12f)
            .build()
        googleMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSuccess() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onError() {
    }

    override fun onNoInternet() {
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
    }
}

interface LocationNavigator {
    fun onSuccess()
    fun onError()
    fun onNoInternet()
}