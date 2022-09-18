package com.example.weatherapp.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.CustomApplication
import com.example.weatherapp.R
import com.example.weatherapp.adapter.SummarisedForecastRecyclerviewAdapter
import com.example.weatherapp.adapter.DetailedForecastRecyclerviewAdapter
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.db.entity.ForecastEntity
import com.example.weatherapp.models.DetailedForecastModel
import com.example.weatherapp.models.ForecastModel
import com.example.weatherapp.util.Constants
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainInterface {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val forecastRecyclerviewAdapter = SummarisedForecastRecyclerviewAdapter()
    private val summaryRecyclerviewAdapter = DetailedForecastRecyclerviewAdapter()

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as CustomApplication).getComponent().inject(this)

        binding.viewModel = viewModel
        viewModel.initiate(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(applicationContext, Constants.PLACES_API)

        binding.location.setOnClickListener {
            if ((application as CustomApplication).isNetworkConnected(this)) {

                if (latitude == null) {
                    Toast.makeText(this, getString(R.string.app_error_restart), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val bundle = Bundle()
                    bundle.putDouble("latitude", latitude!!)
                    bundle.putDouble("longitude", longitude!!)
                    val intent = Intent(this@MainActivity, LocationActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, getString(R.string.internet_required), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        getPlace()
        showWeatherAndForecastData()

    }

    private fun showWeatherAndForecastData() {
        binding.forecastRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = forecastRecyclerviewAdapter
        }

        binding.summaryRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = summaryRecyclerviewAdapter
        }
    }

    private fun getPlace() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, Constants.PLACES_API)
        }

        val autocompleteSupportFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteSupportFragment!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL
            )
        )

        autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng

                viewModel.getDetailedForecastFromApi(latLng?.latitude, latLng?.longitude, true)
                viewModel.getWeatherApi(latLng?.latitude, latLng?.longitude, true)

            }

            override fun onError(status: Status) {
            }
        })
    }

    override fun onSuccess(status: String?) {

        when (status) {
            Constants.CLOUDS -> {
                binding.headerImage.setImageResource(R.drawable.forest_cloudy)
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_cloudy
                    )
                )
                binding.bottomLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_cloudy
                    )
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = ContextCompat.getColor(
                        this,
                        R.color.color_cloudy
                    )
                }

                binding.toolbarLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_cloudy
                    )
                )

                binding.lastUpdated.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_cloudy
                    )
                )
            }
            Constants.RAIN -> {
                binding.headerImage.setImageResource(R.drawable.forest_rainy)
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_rainy
                    )
                )
                binding.bottomLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_rainy
                    )
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = ContextCompat.getColor(
                        this,
                        R.color.color_rainy
                    )
                }
                binding.toolbarLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_rainy
                    )
                )

                binding.lastUpdated.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_rainy
                    )
                )
            }
            Constants.CLEAR -> {
                binding.headerImage.setImageResource(R.drawable.forest_sunny)
                binding.mainLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_sunny
                    )
                )
                binding.bottomLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_sunny
                    )
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = ContextCompat.getColor(
                        this,
                        R.color.color_sunny
                    )

                    binding.toolbarLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_sunny
                        )
                    )

                    binding.lastUpdated.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.color_sunny
                        )
                    )
                }
            }
        }
    }

    override fun onInsertForecastSuccess() {
    }

    override fun onInsertDetailedForecastSuccess() {
    }

    override fun onInsertDetailedForecastError() {
    }

    override fun onInsertForecastError(it: Throwable) {
    }

    override fun onGetApiWeatherSuccess(currentWeather: ApiCurrent?, isSearch: Boolean) {
        if (!isSearch) {
            viewModel.deleteWeather(currentWeather)
            latitude = currentWeather?.coord?.lat
            longitude = currentWeather?.coord?.lon
        }
    }

    override fun onSelectForecastSuccess(forecastEntity: List<ForecastEntity>) {
    }

    override fun onError(t: Throwable) {
        Toast.makeText(
            this,
            getString(R.string.app_error_restart),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onForecastSuccess(
        forecasts: List<ForecastModel>?,
        detailedForecasts: List<DetailedForecastModel>?,
        isSearch: Boolean
    ) {
        forecastRecyclerviewAdapter.notifyDataSetChanged()
        summaryRecyclerviewAdapter.notifyDataSetChanged()

        if (!isSearch) {
            viewModel.deleteAllForecast(forecasts)
            viewModel.deleteDetailedForecasts(detailedForecasts)
        }

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
                    viewModel.setPermissionStatus(true)
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
                viewModel.getWeatherApi(location.latitude, location.longitude, false)
                viewModel.getDetailedForecastFromApi(location.latitude, location.longitude, false)
            } else {
                //Toast.makeText(this, "No known location", Toast.LENGTH_SHORT).show()
                viewModel.selectWeatherLocalDB()
                viewModel.selectDetailedForecastsLocalDB()
                viewModel.selectForecastLocalDB()
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
    fun onInsertForecastSuccess()
    fun onInsertDetailedForecastSuccess()
    fun onInsertDetailedForecastError()
    fun onInsertForecastError(it: Throwable)
    fun onGetApiWeatherSuccess(currentWeather: ApiCurrent?, isSearch: Boolean)
    fun onSelectForecastSuccess(forecastEntity: List<ForecastEntity>)
    fun onError(t: Throwable)
    fun onForecastSuccess(
        forecasts: List<ForecastModel>?,
        detailedForecasts: List<DetailedForecastModel>?,
        isSearch: Boolean
    )
}