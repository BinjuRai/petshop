package com.example.mobileapplication.ui.fragment

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.FragmentProfileBinding
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.ui.activity.EditProfileActivity
import com.example.mobileapplication.ui.activity.LoginActivity
import com.example.mobileapplication.ui.activity.admin.CategoryDashBoardActivity
import com.example.mobileapplication.ui.activity.admin.ProductDashboardActivity
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import kotlin.math.sqrt


class ProfileFragment : Fragment(),SensorEventListener {
    lateinit var profileBinding:FragmentProfileBinding
    lateinit var authViewModel: AuthViewModel
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var lastShakeTime: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        profileBinding=FragmentProfileBinding.inflate(inflater,container,false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var repo = AuthRepoImpl(FirebaseAuth.getInstance(), )
        authViewModel= AuthViewModel(repo)

        var currentUser =authViewModel.getCurrentUser()
        currentUser.let{
            authViewModel.fetchUserData(currentUser?.uid.toString())
        }
        profileBinding.editProfileCard.setOnClickListener{
            var intent=Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("userData",authViewModel.userData.value)
            startActivity(intent)
        }

        sensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
        if(!checkSensor()){
            return
        }else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        }



        profileBinding.editcategoryadmin.setOnClickListener{
            var intent=Intent(requireContext(),CategoryDashBoardActivity::class.java)
            startActivity(intent)
        }

        profileBinding.editproductadmin.setOnClickListener{
            var intent=Intent(requireContext(),ProductDashboardActivity::class.java)
            startActivity(intent)
        }
        authViewModel.userData.observe(requireActivity()){
            users->
            users.let{
                if (users?.imageUrl==null || users.imageUrl.isEmpty()){
                    profileBinding.profileImage.setImageResource(R.drawable.dummyprofilepic)
                    profileBinding.progressBarImage.visibility=View.GONE
                }else{
                    Picasso.get().load(users.imageUrl).into(profileBinding.profileImage,object:Callback{
                        override fun onSuccess() {
                            profileBinding.progressBarImage.visibility=View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Toast.makeText(requireContext(),e?.message,Toast.LENGTH_LONG).show()
                        }

                    })


                }
                profileBinding.EmailProfile.text=users?.email.toString()
                profileBinding.FullNameProfile.text=users?.name.toString()

            }
        }

      profileBinding.editLogout.setOnClickListener{
          logout()

      }
    }
    private fun checkSensor() : Boolean{
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
            flag = false
        }
        return flag
    }

    fun logout(){
        authViewModel.logout{
                success,message->
            if (success){
                var intent=Intent(requireContext(),LoginActivity::class.java)
                startActivity(intent)
                requireActivity()
                    .finish()
            }else{
                Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values
        val xAxis = values[0]
        val yAxis = values[1]
        val zAxis = values[2]


        detectShake(xAxis, yAxis, zAxis)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun detectShake(x: Float, y: Float, z: Float) {
        val accelerationMagnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val shakeThreshold = 12.0f
        val currentTime = System.currentTimeMillis()
        if (accelerationMagnitude > shakeThreshold) {
            if (currentTime - lastShakeTime > 500) { // 500 ms delay to prevent multiple triggers
                lastShakeTime = currentTime
                logout()
            }
        }
    }


}