package com.britishbroadcast.myservice.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.britishbroadcast.myservice.R
import com.britishbroadcast.myservice.model.Song

class PlayMusicService : Service() {

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

    fun chooseSong(song: Song) {
//        if(mediaPlayer.isPlaying){
//            mediaPlayer.reset()
        //mediaPlayer.setDataSource(uri / path / etc)
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(this, song.songResource)
        mediaPlayer.start()
//        }

        createNotificationChannel(song)
    }


    private lateinit var notificationManager: NotificationManager
    private fun createNotificationChannel(song: Song) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    "MY_NOTIFICATION_ID",
                    "com.britishbroadcast",
                    NotificationManager.IMPORTANCE_LOW
            )

            notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat
                .Builder(applicationContext, "MY_NOTIFICATION_ID")
                .setSmallIcon(R.drawable.ic_album)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.flame))
                .setContentTitle(song.songInfo)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

        //Foreground Service has to have an ID for OS purposes
        startForeground(101, notification)
        val notificationCompat = NotificationManagerCompat.from(this)
        notificationCompat.notify(101, notification)
    }

    override fun onTaskRemoved(rootIntent: Intent) {

        Log.d(TAG, "onTaskRemoved: ")

        if(mediaPlayer.isPlaying)
            mediaPlayer.stop()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            stopForeground(STOP_FOREGROUND_REMOVE)

        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    fun playSong() {
        if (!mediaPlayer.isPlaying)
            mediaPlayer.start()
    }

    fun pauseSong() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
    }

    inner class PlayBinder : Binder() {
        fun getPlayInstance(): PlayMusicService = this@PlayMusicService
    }
}