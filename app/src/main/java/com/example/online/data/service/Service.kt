package com.example.online.data.service

import com.example.online.data.model.Horoscope
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface Service {
    @POST("/")
    suspend fun getData(@Query("sign") sign: String): Response<Horoscope>
}