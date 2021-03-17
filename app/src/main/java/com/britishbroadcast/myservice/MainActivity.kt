package com.britishbroadcast.myservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.britishbroadcast.myservice.databinding.ActivityMainBinding
import com.britishbroadcast.myservice.model.Song
import com.britishbroadcast.myservice.service.PlayMusicService
import com.britishbroadcast.myservice.service.PlayerFragment
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity(), MusicAdapter.SongDelegate, PlayerFragment.PlayInterface {

    private val songList = mutableListOf<Song>(
        Song("https://musiconcd.eu/wp-content/uploads/2020/03/8718627230541.jpg","Confessions by DaveJF", R.raw.davejf__confessions),
        Song("https://townsquare.media/site/812/files/2017/07/100000x100000-999.jpg?w=980&q=75","Synth by Mative", R.raw.mative_synth)
    )
    private lateinit var playService: PlayMusicService

    private val serviceConnection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName) {
            //Called when an error occurs...
        }
        override fun onServiceConnected(name: ComponentName, service: IBinder?) {
            //Called when the service has been successfully bound to
            Log.d("TAG_X", "onServiceConnected: ")
            playService = (service as PlayMusicService.PlayBinder).getPlayInstance()
        }
    }

    private lateinit var playServiceIntent: Intent
    private lateinit var binding: ActivityMainBinding
    private val musicAdapter = MusicAdapter(songList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.musicRecyclerview.adapter = musicAdapter


        playServiceIntent = Intent(this, PlayMusicService::class.java)
        startService(playServiceIntent)//a started service can become a bound...
//        Handler(mainLooper).postDelayed({
        bindService(playServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)//Bind to service..
//        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        //stopService(playServiceIntent)
        unbindService(serviceConnection) //how to unbind from service
    }

    override fun playSong(song: Song) {
        if(this::playService.isInitialized){
            songIndex = songList.indexOf(song)
            playService.chooseSong(song)
            playingBoolean.set(true)

            displayPlayFragment(song)
        }
    }

    private val songFragment = PlayerFragment()

    private fun displayPlayFragment(song: Song) {

        supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, songFragment.also {

                val bundle = Bundle()
                bundle.putParcelable("SONG", song)
                it.arguments = bundle

            })
            .addToBackStack(songFragment.tag)
            .commit()
    }

    private var playingBoolean = AtomicBoolean(false)

    override fun pausePlayCurrentSong() {
        if(this::playService.isInitialized){
            if(playingBoolean.getAndSet(false)) {
                playService.pauseSong()
            } else{
                playingBoolean.set(true)
                playService.playSong()
            }
        }
    }
    var songIndex = 0

    override fun nextSong() {
        if(this::playService.isInitialized){
            songIndex++
            if(songIndex == songList.size)
                songIndex = 0
            changeSong()
        }
    }

    override fun previousSong() {
        if(this::playService.isInitialized){
            songIndex--
            if(songIndex < 0)
                songIndex = songList.size-1
            changeSong()
        }
    }

    private fun changeSong() {
        songList[songIndex].let {
            songFragment.updateSongUI(it)
            playService.chooseSong(it)
            playingBoolean.set(true)
        }
    }

    override fun isPlaying(): Boolean = playingBoolean.get()

}