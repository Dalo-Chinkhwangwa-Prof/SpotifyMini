package com.britishbroadcast.myservice.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.britishbroadcast.myservice.R
import com.britishbroadcast.myservice.model.Song

class PlayMusicService: Service() {

    val TAG = "TAG_X"
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        mediaPlayer = MediaPlayer.create(this, R.raw.davejf__confessions)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ")
        //mediaPlayer.start()
        return PlayBinder()
    }

    fun chooseSong(song: Song){
//        if(mediaPlayer.isPlaying){
//            mediaPlayer.reset()
            //mediaPlayer.setDataSource(uri / path / etc)
            mediaPlayer.reset()
            mediaPlayer = MediaPlayer.create(this, song.songResource)
            mediaPlayer.start()
//        }
    }

    fun playSong() {
        if(!mediaPlayer.isPlaying)
            mediaPlayer.start()
    }

    fun pauseSong(){
        if(mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }
    
    inner class PlayBinder: Binder() {
        fun getPlayInstance(): PlayMusicService = this@PlayMusicService
    }
}