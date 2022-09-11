package com.example.weatherapp

import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .method(original.method(), original.body())

        return chain.proceed(
            request.build()
        )

    }
}