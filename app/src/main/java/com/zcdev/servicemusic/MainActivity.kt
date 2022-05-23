package com.zcdev.servicemusic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicp.MusicService
import com.zcdev.servicemusic.data.models.Music
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mediaPlayer: MediaPlayer
    private val REQUEST_CODE = 1
    var i = 0
    var musics: ArrayList<Music>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        permission()
        getAllAudio()
        mediaPlayer=MediaPlayer()
     //   musics= ArrayList()
        // next
        imageNext.setOnClickListener(View.OnClickListener {
            if (i < musics!!.size) {
                i=i+1

                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.seekTo(0)

                }
                tvmName.setText(musics!![i].title.toString())

                mediaPlayer.setDataSource(musics!![i].path.toString())
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }
            } else {
                i += 0
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.seekTo(0)
                }
                tvmName.setText(musics!![i].title.toString())
                mediaPlayer.setDataSource(musics!![i].path.toString())
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }

            }
        })

        //prev
        imagePrev.setOnClickListener(View.OnClickListener {
            if (i < musics!!.size) {
                i=i+1

                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.seekTo(0)

                }
                tvmName.setText(musics!![i].title.toString())
                mediaPlayer.setDataSource("/storage/emulated/0/VoiceRecorder/test.mp3")
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }
            } else {
                i += 0
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.seekTo(0)
                }
                tvmName.setText(musics!![i].title.toString())
                mediaPlayer.setDataSource("/storage/emulated/0/VoiceRecorder/test.mp3")
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    mediaPlayer.start()
                }

            }
        })


        //play
        imagePlay.setOnClickListener(View.OnClickListener {
            startService(Intent(this@MainActivity, MusicService::class.java))
            tvmName.setText(musics!![i].title.toString())
            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
                mediaPlayer.seekTo(0)

            }
            mediaPlayer.setDataSource(musics!![i].path.toString())
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }

        })
    }





    private fun permission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            Toast.makeText(this, "Permission Granted !!", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, getAllAudio()!!.size.toString(), Toast.LENGTH_SHORT).show()
            getAllAudio()
        }
    }



    fun getAllAudio(): ArrayList<Music> {
        val audioList: ArrayList<Music> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE
        )
        val cursor: Cursor = applicationContext.getContentResolver().query(uri, projection, null, null, null)!!
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Create a model object.
                // Create a model object.
                val audioModel = Music()

                val path: String = cursor.getString(0) // Retrieve path.

                val name: String = cursor.getString(1) // Retrieve name.

                // Set data to the model object.

                // Set data to the model object.
                audioModel.title=name
                audioModel.path=path
                audioList!!.add(audioModel)
            }
            cursor.close()
            musics=audioList
        }
        return audioList
    }
}

