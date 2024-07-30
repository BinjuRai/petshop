package com.example.mobileapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivitySplashScrenBinding

class SplashScrenActivity : AppCompatActivity() {
    lateinit var splashScrenBinding: ActivitySplashScrenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScrenBinding=ActivitySplashScrenBinding.inflate(layoutInflater)
        setContentView(splashScrenBinding.root)

        splashScrenBinding.skipbtn1.setOnClickListener{
            var intent= Intent(this@SplashScrenActivity,SplashScreeenActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}