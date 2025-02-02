package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityForgetpasswordBinding
import com.example.mobileapplication.repository.ForegetRepoImpl
import com.example.mobileapplication.repository.ForgetRepo
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.example.mobileapplication.viewmodel.ForgetViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ForgetpasswordActivity : AppCompatActivity() {
    lateinit var forgetpasswordBinding: ActivityForgetpasswordBinding
    lateinit var forgetViewModel: ForgetViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        forgetpasswordBinding=ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(forgetpasswordBinding.root)

        var repo = ForegetRepoImpl(FirebaseAuth.getInstance(),)
        forgetViewModel=ForgetViewModel(repo)

        loadingUtils = LoadingUtils(this)
        forgetpasswordBinding.forgetpsbtn.setOnClickListener {
            loadingUtils.showDialog()
            var email:String = forgetpasswordBinding.forgetpsEmailAddress.text.toString()

            forgetViewModel.forgetpassword(email){
                    success,message->
                if(success){
                    loadingUtils.dismiss()
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    loadingUtils.dismiss()
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()

                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}