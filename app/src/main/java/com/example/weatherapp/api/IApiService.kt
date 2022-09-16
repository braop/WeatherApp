package com.example.weatherapp.api

import com.example.weatherapp.CustomInterceptor
import com.example.weatherapp.api.response.ApiCurrent
import com.example.weatherapp.api.response.ApiForecast
import com.example.weatherapp.util.Constants
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class ApiService {

    val service: IApiService = create(IApiService::class.java)

    lateinit var retrofit: Retrofit

    private fun <S> create(serviceClass: Class<S>): S {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .create()

        //create retrofit
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
            .client(httpBuilder.build())
            .build()

        return retrofit.create(serviceClass)
    }

    private val httpBuilder: OkHttpClient.Builder
        get() {
            //create http client
            return OkHttpClient.Builder()
                .addInterceptor(CustomInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
        }

    interface IApiService {
        @GET("forecast")
        fun getForecast(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double,
            @Query("appid") appid: String,
            @Query("units") metric: String
        ): Call<ApiForecast>

        @GET("weather")
        fun getWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("appid") appid: String,
            @Query("units") metric: String
        ): Call<ApiCurrent>
    }

}