package com.example.mobileapplication.ui.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityDashboardBinding
import com.example.mobileapplication.ui.fragment.CalendarFragment
import com.example.mobileapplication.ui.fragment.HomeFragment
import com.example.mobileapplication.ui.fragment.ProfileFragment
import com.example.mobileapplication.ui.fragment.TimerFragment
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {

    lateinit var dashboardBinding: ActivityDashboardBinding

    private fun replaceFragemnt(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragementTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragementTransaction.replace(R.id.frameLayout, fragment)
        fragementTransaction.commit()
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            dashboardBinding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(dashboardBinding.root)

            replaceFragemnt(HomeFragment())
            dashboardBinding.btnNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menuHome -> replaceFragemnt(HomeFragment())
                    R.id.menuCalander -> replaceFragemnt(CalendarFragment())
                    R.id.menuTimer -> replaceFragemnt(TimerFragment())
                    R.id.menuProfile -> replaceFragemnt(ProfileFragment())
                    else -> {}
                }
                true
            }

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                insets
            }
        }


}