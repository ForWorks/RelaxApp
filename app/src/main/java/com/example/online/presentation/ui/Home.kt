package com.example.online.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.online.R
import com.example.online.data.model.Advice
import com.example.online.presentation.viewModels.UserViewModel
import com.example.online.data.model.Music
import com.example.online.data.model.States.*
import com.example.online.presentation.adapters.StateAdapter
import com.example.online.databinding.FragmentHomeBinding
import com.example.online.presentation.adapters.AdviceAdapter
import com.example.online.presentation.viewModels.MusicViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val musicViewModel: MusicViewModel by activityViewModels()
    companion object { private var list = mutableListOf<Advice>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.navigationView)?.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicList: List<Music> = listOf(
            Music(Calm.name, R.raw.sound_1),
            Music(Relaxed.name, R.raw.sound_2),
            Music(Focused.name, R.raw.sound_3),
            Music(Excited.name, R.raw.sound_4)
        )

        binding.advices.adapter = AdviceAdapter(list)
        binding.states.adapter = StateAdapter {
            list = it.first
            when(it.second) {
                Calm.name -> musicViewModel.music.value = musicList[0]
                Relaxed.name -> musicViewModel.music.value = musicList[1]
                Focused.name -> musicViewModel.music.value = musicList[2]
                Excited.name -> musicViewModel.music.value = musicList[3]
            }
            binding.advices.adapter = AdviceAdapter(it.first)
        }

        userViewModel.user.observe(viewLifecycleOwner) {
            binding.greeting.text = activity?.resources?.getString(R.string.greeting).plus(it?.name)
            Glide.with(binding.root)
                .load(it?.avatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.homeAvatar)
        }
    }
}