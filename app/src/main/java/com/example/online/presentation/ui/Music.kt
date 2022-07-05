package com.example.online.presentation.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.online.data.model.Music
import com.example.online.databinding.FragmentMusicBinding
import com.example.online.presentation.viewModels.MusicViewModel

class Music : Fragment() {

    private lateinit var binding: FragmentMusicBinding
    private val musicViewModel: MusicViewModel by activityViewModels()
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMusicBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicCurrent = musicViewModel.music.value!!

        if(music == null)
            binding.musicName.text = musicCurrent.name
        else
            binding.musicName.text = music!!.name

        if(player != null) {
            binding.progressBar.progress = player!!.currentPosition
            binding.musicTime.text = fixTime(player!!.duration)
            binding.progressBar.max = player!!.duration
        }

        binding.musicStart.setOnClickListener {
            if(music != musicCurrent && player != null) {
                player?.stop()
                player?.release()
                player = MediaPlayer.create(activity?.applicationContext, musicCurrent.resource)
                notifyMusic()
            }

            if(player == null) {
                player = MediaPlayer.create(activity?.applicationContext, musicCurrent.resource)
                notifyMusic()
            }

            player?.start()
        }

        binding.musicPause.setOnClickListener { player?.pause() }

        binding.musicStop.setOnClickListener {
            if(player != null) {
                player?.stop()
                player?.release()
                binding.progressBar.progress = 0
                binding.musicCurrentTime.text = ZERO
                player = null
            }
        }
    }

    private fun notifyMusic() {
        val musicCurrent = musicViewModel.music.value!!
        music = musicCurrent
        binding.musicName.text = musicCurrent.name
        binding.progressBar.progress = player!!.currentPosition
        binding.progressBar.max = player!!.duration
        binding.musicTime.text = fixTime(player!!.duration)
    }

    private fun fixTime(position: Int): String {
        var seconds = ((position % 60000) / 1000).toString()
        if(seconds.toInt() < 10)
            seconds = ZERO.plus(seconds)
        return String.format(FORMAT, position / 60000, seconds)
    }

    private val showProgress = object : Runnable {
        override fun run() {
            if(player != null) {
                binding.progressBar.progress = player!!.currentPosition
                binding.musicCurrentTime.text = fixTime(player!!.currentPosition)
            }
            handler.postDelayed(this, 1000)
        }
    }

    override fun onStart() {
        super.onStart()
        handler.post(showProgress)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(showProgress)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showProgress)
    }

    companion object {
        private var player: MediaPlayer? = null
        private var music: Music? = null
        private const val ZERO = "0"
        private const val FORMAT = "%d.%s"
    }
}
