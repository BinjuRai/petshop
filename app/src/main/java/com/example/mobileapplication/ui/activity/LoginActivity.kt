package com.example.mobileapplication.ui.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityLoginBinding
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class LoginActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var loadingUtils: LoadingUtils
    private val channelID = "1"
    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        // Initialize the proximity sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (checkProximitySensor()) {
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show()
        }

        // Initialize ViewModel and other utilities
        val repo = AuthRepoImpl(FirebaseAuth.getInstance())
        authViewModel = AuthViewModel(repo)
        loadingUtils = LoadingUtils(this)

        // Set up login button click listener
        loginBinding.loginbtn.setOnClickListener {
            performLogin()
        }

        // Set up other buttons click listeners
        loginBinding.newaccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

        loginBinding.forgetps.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetpasswordActivity::class.java))
        }

        // Handle window insets for proper layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun performLogin() {
        val email = loginBinding.EmailAddressLogin.text.toString().trim()
        val password = loginBinding.PasswordLogin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        loadingUtils.showDialog()

        authViewModel.login(email, password) { success, message ->
            loadingUtils.dismiss()
            if (success) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                showNotification()
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkProximitySensor(): Boolean {
        return sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val proximityValue = it.values[0]
            if (proximityValue < proximitySensor?.maximumRange ?: 0f) {
                performLogin()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if necessary
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the sensor listener to prevent memory leaks
        sensorManager.unregisterListener(this)
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this@LoginActivity, channelID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, "Login Notifications", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Furry House")
            .setContentText("New session detected")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this@LoginActivity)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(0, builder.build())
        }
    }
}

