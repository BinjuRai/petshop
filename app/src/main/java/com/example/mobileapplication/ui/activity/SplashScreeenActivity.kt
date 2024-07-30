package com.example.mobileapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivitySplashScreeenBinding

class SplashScreeenActivity : AppCompatActivity() {

    lateinit var splashScreeenBinding: ActivitySplashScreeenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreeenBinding=ActivitySplashScreeenBinding.inflate(layoutInflater)
        setContentView(splashScreeenBinding.root)

        splashScreeenBinding.skipBtn2.setOnClickListener{
            var intent= Intent(this@SplashScreeenActivity,SplashScreenActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}