package com.example.online.presentation.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.online.R
import com.example.online.presentation.viewModels.UserViewModel
import com.example.online.data.model.User
import com.example.online.databinding.FragmentRegistrationBinding
import com.example.online.domain.utils.Constants.DATABASE_NAME
import com.example.online.domain.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Registration : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.navigationView)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RegButton.setOnClickListener {
            val login = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()

            if(checkData(login, password)) {
                Firebase.auth.createUserWithEmailAndPassword(login, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = User(
                                binding.emailField.text.toString().hashCode().toString(),
                                binding.nameField.text.toString(),
                                binding.emailField.text.toString()
                            )

                            Firebase.firestore.collection(DATABASE_NAME)
                                .add(user)
                                .addOnSuccessListener {
                                    userViewModel.user.value = user
                                    Navigation.findNavController(binding.root).navigate(R.id.homeFragment)
                                }
                        } else
                            activity?.resources?.getString(R.string.failed)?.let { toast(binding.root.context, it) }
                    }
            }
        }
    }

    private fun checkData(login: String, password: String): Boolean {
        if(login.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login).matches())
            return true
        else
            activity?.resources?.getString(R.string.failed)?.let { toast(binding.root.context, it) }
        return false
    }
}