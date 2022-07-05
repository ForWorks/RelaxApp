package com.example.online.data.repository

import com.example.online.data.model.Horoscope
import com.example.online.data.service.Service

class Repository(private val service: Service) {
    suspend fun getData(sign: String): Horoscope? {
        return service.getData(sign).body()
    }
}