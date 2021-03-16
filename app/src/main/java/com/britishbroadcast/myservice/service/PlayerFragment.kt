package com.britishbroadcast.myservice.service

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.britishbroadcast.myservice.MainActivity
import com.britishbroadcast.myservice.databinding.SongPlayFragmentBinding
import com.britishbroadcast.myservice.model.Song
import com.squareup.picasso.Picasso

class PlayerFragment : Fragment() {

    interface PlayInterface {
        fun pausePlayCurrentSong()
        fun nextSong()
        fun previousSong()
    }

    private lateinit var playInterface: PlayInterface

    private lateinit var binding: SongPlayFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SongPlayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            val song = it.get("SONG") as Song

            binding.apply {

                Picasso.get()
                    .load(song.albumArt)
                    .resize(80, 80)
                    .centerCrop()
                    .into(artwork)

                songInfoTextview.text = song.songInfo
            }

        }

        binding.apply {
            nextImageview.setOnClickListener {
                playInterface.nextSong()
            }

            prevImageview.setOnClickListener {
                playInterface.previousSong()
            }

            playPauseImageview.setOnClickListener {
                playInterface.pausePlayCurrentSong()
            }
        }
    }

    override fun onAttach(context: Context) {//called when the activity has attached the fragment
        super.onAttach(context)
        playInterface = (context as MainActivity)
    }
}