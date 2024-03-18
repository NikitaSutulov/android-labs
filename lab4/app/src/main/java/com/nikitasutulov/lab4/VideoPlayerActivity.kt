package com.nikitasutulov.lab4

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts

class VideoPlayerActivity : AppCompatActivity() {

    private val videoView: VideoView by lazy { findViewById(R.id.videoView) }

    private val storageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val storageType = intent.getStringExtra("storageType")
        val link = intent.getStringExtra("link")

        when (storageType) {
            MainActivity.STORAGE_INTERNAL -> {
                videoView.setVideoPath(filesDir.absolutePath + "/" + link)
            }
            MainActivity.STORAGE_EXTERNAL -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        requestPermission()
                    }
                }

                videoView.setVideoPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/" + link)
            }
            else -> {
                videoView.setVideoURI(Uri.parse(link))
            }
        }
        videoView.start()
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
}