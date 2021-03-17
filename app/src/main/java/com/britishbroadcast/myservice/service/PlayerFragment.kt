package com.britishbroadcast.myservice.service

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.britishbroadcast.myservice.MainActivity
import com.britishbroadcast.myservice.R
import com.britishbroadcast.myservice.databinding.SongPlayFragmentBinding
import com.britishbroadcast.myservice.model.Song
import com.squareup.picasso.Picasso
import java.util.concurrent.atomic.AtomicBoolean

class PlayerFragment : Fragment() {

    interface PlayInterface {
        fun pausePlayCurrentSong()
        fun nextSong()
        fun previousSong()
        fun isPlaying(): Boolean
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

    fun updateSongUI(song: Song){

        binding.apply {

            Picasso.get()
                    .load(song.albumArt)
                    .resize(80, 80)
                    .centerCrop()
                    .into(artwork)

            songInfoTextview.text = song.songInfo
            playPauseImageview.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val song = it.get("SONG") as Song
            updateSongUI(song)
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

                if(playInterface.isPlaying())
                    playPauseImageview.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pause))
                else
                    playPauseImageview.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_play))
            }
        }
    }

    override fun onAttach(context: Context) {//called when the activity has attached the fragment
        super.onAttach(context)
        playInterface = (context as MainActivity)
    }
}