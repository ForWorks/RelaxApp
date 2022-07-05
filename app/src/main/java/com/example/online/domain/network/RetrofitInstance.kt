package com.example.online.domain.network

import com.example.online.data.service.Service
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient
    .Builder()
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://aztro.sameerkumar.website")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val api by lazy {
        retrofit.create(Service::class.java)
    }

    suspend fun getData(body: String) = api.getData(body).body()
}