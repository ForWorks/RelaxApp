package com.example.online.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.online.R
import com.example.online.data.model.Music
import com.example.online.data.model.States.Calm

class MusicViewModel: ViewModel() {
    val music: MutableLiveData<Music> = MutableLiveData(Music(Calm.name, R.raw.sound_1))
}