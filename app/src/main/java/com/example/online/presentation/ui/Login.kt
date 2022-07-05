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
import com.example.online.databinding.FragmentLoginBinding
import com.example.online.domain.utils.Constants.DATABASE_NAME
import com.example.online.domain.utils.Constants.USER_AGE
import com.example.online.domain.utils.Constants.USER_AVATAR
import com.example.online.domain.utils.Constants.USER_EMAIL
import com.example.online.domain.utils.Constants.USER_HEIGHT
import com.example.online.domain.utils.Constants.USER_NAME
import com.example.online.domain.utils.Constants.USER_QUERY
import com.example.online.domain.utils.Constants.USER_WEIGHT
import com.example.online.domain.utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.findViewById<BottomNavigationView>(R.id.navigationView)?.visibility = View.GONE
        setListeners()
    }

    private fun setListeners() {

        binding.signInButton.setOnClickListener {
            val login = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            val id = login.hashCode().toString()

            if(checkData(login, password)) {
                Firebase.auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Firebase.firestore.collection(DATABASE_NAME).whereEqualTo(USER_QUERY, id).get()
                            .addOnSuccessListener { result ->
                                val document = result.documents[0]
                                val user = User(
                                    id,
                                    document[USER_NAME].toString(),
                                    document[USER_EMAIL].toString(),
                                    document[USER_AVATAR].toString(),
                                    document[USER_AGE].toString(),
                                    document[USER_WEIGHT].toString(),
                                    document[USER_HEIGHT].toString()
                                )
                                userViewModel.user.value = user
                                Navigation.findNavController(binding.root).navigate(R.id.homeFragment)
                            }
                    } else
                        activity?.resources?.getString(R.string.failed)?.let { toast(binding.root.context, it) }
                }
            }
        }

        binding.registerButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.registrationFragment)
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