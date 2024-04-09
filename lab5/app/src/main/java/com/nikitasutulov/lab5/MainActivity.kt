package com.nikitasutulov.lab5

import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SensorEventListener, OnClickListener {

    private val sensorManager: SensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    private var isRunning = false
    private var totalStepsCount = 0
    private var previousTotalStepsCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                123) // some random magic number

        }

        findViewById<View>(R.id.resetButton).setOnClickListener(this)
        loadStepsCount()
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCounterSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
        saveStepsCount()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (isRunning) {
            Log.d("steps", sensorEvent!!.values[0].toString())
            totalStepsCount = sensorEvent!!.values[0].toInt()
            val currentStepsCount = totalStepsCount - previousTotalStepsCount
            findViewById<TextView>(R.id.textView).text = "Total steps: $currentStepsCount of 1000"
            findViewById<ProgressBar>(R.id.progressBar).progress = currentStepsCount
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.resetButton -> {
                previousTotalStepsCount = totalStepsCount
                findViewById<TextView>(R.id.textView).text = "Total steps: 0 of 1000"
                findViewById<ProgressBar>(R.id.progressBar).progress = 0
            }
        }
    }

    private fun saveStepsCount() {
        val sharedPreferences = getSharedPreferences("stepsPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("stepsCount", previousTotalStepsCount)
        editor.apply()
    }

    private fun loadStepsCount() {
        val sharedPreferences = getSharedPreferences("stepsPrefs", MODE_PRIVATE)
        previousTotalStepsCount = sharedPreferences.getInt("stepsCount", 0)
    }
}