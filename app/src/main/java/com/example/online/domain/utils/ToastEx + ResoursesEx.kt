package com.example.online.domain.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.resources(id: Int): String? {
    return resources?.getString(id)
}