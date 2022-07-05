package com.example.online.data.model

import java.io.Serializable

data class User (
    val id: String,
    var name: String,
    val email: String,
    var avatar: String = "",
    var age: String = "",
    var weight: String = "",
    var height: String = ""
): Serializable
