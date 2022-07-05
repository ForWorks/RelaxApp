package com.example.online.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.online.R
import com.example.online.databinding.FragmentPreLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PreLogin : Fragment() {

    private lateinit var binding: FragmentPreLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPreLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.findViewById<BottomNavigationView>(R.id.navigationView)?.visibility = View.GONE

        binding.signIn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.loginFragment)
        }

        binding.noAccountText.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.registrationFragment)
        }
    }
}