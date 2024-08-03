package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityForgetpasswordBinding
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel

class ForgetpasswordActivity : AppCompatActivity() {
    lateinit var forgetpasswordBinding: ActivityForgetpasswordBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        forgetpasswordBinding=ActivityForgetpasswordBinding.inflate(layoutInflater)
        setContentView(forgetpasswordBinding.root)

        var repo = AuthRepoImpl()
        authViewModel=AuthViewModel(repo)

        loadingUtils = LoadingUtils(this)
        forgetpasswordBinding.forgetpsbtn.setOnClickListener {
            loadingUtils.showDialog()
            var email:String = forgetpasswordBinding.forgetpsEmailAddress.text.toString()

            authViewModel.forgetpassword(email){
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



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.LoginPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}