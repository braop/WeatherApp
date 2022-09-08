package com.example.weatherapp.api

import android.content.Context
import com.example.weatherapp.CustomInterceptor
import com.example.weatherapp.R
import com.example.weatherapp.api.response.ApiForecast
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


class ApiService(private val context: Context) {

    val service: IApiService = create(IApiService::class.java)

    lateinit var retrofit: Retrofit

    fun <S> create(serviceClass: Class<S>): S {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .create()

        //create retrofit
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(context.getString(R.string.base_url))
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
        @GET("forecast?id=524901&appid=63f6a732652e0076ef7243052596bcb6")
        fun getForecast(): Call<ApiForecast>

        @GET("weather?lat=0.35123179760823653&lon=32.58288521779097&appid=63f6a732652e0076ef7243052596bcb6")
        fun getCurrent(): ApiForecast
    }

}