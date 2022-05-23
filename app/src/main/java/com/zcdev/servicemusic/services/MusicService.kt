package com.example.musicp

import android.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.zcdev.servicemusic.MainActivity
import com.zcdev.servicemusic.data.models.Music

class MusicService : Service() {
    var tempAudioList: MutableList<Music> = ArrayList<Music>()

    // Method to read all the audio/MP3 files.
    fun getAllAudioFromDevice(context: Context): List<Music> {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val c = context.contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.DATA + " like ? ",
            arrayOf("%Download%"),
            null
        )
        if (c != null) {
            while (c.moveToNext()) {
                // Create a model object.
                val Music = Music()
                val path = c.getString(0) // Retrieve path.
                val name = c.getString(1) // Retrieve name.
              /*  val album = c.getString(2) // Retrieve album name.
                val artist = c.getString(3) // Retrieve artist name.*/

                // Set data to the model object.
            /*    Music.setaName(name)
                Music.setaAlbum(album)
                Music.setaArtist(artist)
                Music.setaPath(path)*/
           /*     Log.e("Name :$name", " Album :$album")
                Log.e("Path :$path", " Artist :$artist")*/

                // Add the model object to the list .
                tempAudioList.add(Music)
            }
            c.close()
        }
        // Return the list.
        return tempAudioList
    }

    private var recv: MyReceiver? = null
    var musicPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        getAllAudioFromDevice(this)
        musicPlayer = MediaPlayer()
        musicPlayer!!.isLooping = false
        recv = MyReceiver()
        registerReceiver(recv, IntentFilter("PlayPause"))
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Music Service started by user.", Toast.LENGTH_LONG).show()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )
        // intent du clique bouton play/pause
        val pPPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("PlayPause"),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Notification Channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "my_channel_id"
        val channelName: CharSequence = "My Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Lecture en cours")
            .setContentText("music")
            .setSmallIcon(R.drawable.ic_media_play)
            .addAction(R.drawable.ic_media_play, "Play/Pause", pPPendingIntent)
            .setContentIntent(pendingIntent) // .setPriority(Notification.PRIORITY_MAX)
            .build()
        startForeground(110, notification)
        musicPlayer!!.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer!!.stop()
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show()
        unregisterReceiver(recv)
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == "PlayPause") {
                if (musicPlayer!!.isPlaying) {
                    musicPlayer!!.pause()
                } else {
                    musicPlayer!!.start()
                }
            }
        }
    }
}