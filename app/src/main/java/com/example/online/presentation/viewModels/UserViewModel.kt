package com.example.online.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.online.data.model.User

class UserViewModel: ViewModel() {
    var user: MutableLiveData<User> = MutableLiveData()
}