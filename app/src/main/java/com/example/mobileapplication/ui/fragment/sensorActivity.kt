package com.example.mobileapplication.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.ui.activity.LoginActivity

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth




class sensorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sensor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class AccelerometerHandler(private val context: Context) : SensorEventListener {

        private lateinit var sensorManager: SensorManager
        private lateinit var accelerometerSensor: Sensor
        private var lastShakeTime: Long = 0

        init {
            initializeSensor()
        }

        private fun initializeSensor() {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                ?: throw UnsupportedOperationException("Accelerometer not supported")

            // Register listener
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val xAxis = it.values[0]
                    val yAxis = it.values[1]
                    val zAxis = it.values[2]

                    Log.d("Accelerometer", "X: $xAxis, Y: $yAxis, Z: $zAxis")

                    // Example: Implement tilt detection
                    detectTilt(xAxis, yAxis, zAxis)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle accuracy changes if needed
        }

        private fun detectTilt(x: Float, y: Float, z: Float) {
            val tiltThreshold = 7.0f // Adjust as needed

            // Check for left tilt
            if (x < -tiltThreshold) {
                logout()
            }

            // Check for right tilt
            if (x > tiltThreshold) {
                logout()
            }
        }

        private fun logout() {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        fun unregisterListener() {
            sensorManager.unregisterListener(this)
        }

        // Clean up resources
        fun cleanup() {
            unregisterListener()
        }
    }

}