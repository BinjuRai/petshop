package com.example.mobileapplication.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityLoginBinding
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        var repo = AuthRepoImpl()
        authViewModel= AuthViewModel(repo)

        loadingUtils=LoadingUtils(this)

        loginBinding.loginbtn.setOnClickListener{
            loadingUtils.showDialog()
            var email:String=loginBinding.EmailAddress.text.toString()
            var password:String=loginBinding.Password.text.toString()


            authViewModel.login(email,password){
                    success,message ->
                if(success){
                    Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                    var intent = Intent(this@LoginActivity,DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }
        }



        loginBinding.newaccount.setOnClickListener{
            var intent= Intent(this@LoginActivity,SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.forgetps.setOnClickListener{
            var intent= Intent(this@LoginActivity,ForgetpasswordActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}