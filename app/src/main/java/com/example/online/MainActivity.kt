package com.example.online

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.online.databinding.ActivityMainBinding
import com.example.online.presentation.viewModels.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_Online)
        setContentView(binding.root)

        findViewById<BottomNavigationView>(R.id.navigationView)?.visibility = View.GONE
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val navController = findNavController(R.id.navigationHost)
        binding.navigationView.setupWithNavController(navController)
    }
}