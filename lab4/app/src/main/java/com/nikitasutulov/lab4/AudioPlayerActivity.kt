package com.nikitasutulov.lab4

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class AudioPlayerActivity : AppCompatActivity() {

    private val storageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    private lateinit var mediaPlayer: MediaPlayer

    private var isAudioPaused = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        mediaPlayer = MediaPlayer().also {
            it.setOnPreparedListener { }
            it.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            it.setOnCompletionListener {
                findViewById<ImageButton>(R.id.playPauseButton).setImageResource(R.drawable.baseline_play_arrow_24)
            }
        }

        val storageType = intent.getStringExtra("storageType")
        val link = intent.getStringExtra("link")

        when (storageType) {
            MainActivity.STORAGE_INTERNAL -> {
                mediaPlayer.setDataSource(filesDir.absolutePath + "/" + link)
            }

            MainActivity.STORAGE_EXTERNAL -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        requestPermission()
                    }
                }

                mediaPlayer.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath + "/" + link)
            }

            else -> {
                mediaPlayer.setDataSource(this, Uri.parse(link))
            }
        }
        mediaPlayer.prepare()
        setButtonsOnClickListeners()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            storageActivityResultLauncher.launch(intent)
        }
    }

    private fun setButtonsOnClickListeners() {
        val playPauseButton = findViewById<ImageButton>(R.id.playPauseButton)
        val stopButton = findViewById<ImageButton>(R.id.stopButton)

        playPauseButton.setOnClickListener {
            if (isAudioPaused) {
                playPauseButton.setImageResource(R.drawable.baseline_pause_24)
                mediaPlayer.start()
            } else {
                playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
                mediaPlayer.pause()
            }
            isAudioPaused = !isAudioPaused
        }

        stopButton.setOnClickListener {
            isAudioPaused = true
            playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
            mediaPlayer.stop()
            mediaPlayer.prepare()
        }
    }
}