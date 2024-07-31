package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivitySignupBinding
import com.example.mobileapplication.model.UserModel
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel


class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding: ActivitySignupBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        signupBinding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)

        var repo = AuthRepoImpl()

        authViewModel= AuthViewModel(repo)

        loadingUtils=LoadingUtils(this)

        signupBinding.Signupbtn.setOnClickListener{
            loadingUtils.showDialog()
            var name:String=signupBinding.editTextFullName.text.toString()
            var phone:String=signupBinding.editTextPhoneNo.text.toString()
            var address:String=signupBinding.editTextAddress.text.toString()
            var email:String=signupBinding.emailSignup.text.toString()
            var password:String=signupBinding.editTextPasswordSignup.text.toString()

            var userModel=UserModel("",name,phone,address,email,"","")

            authViewModel.signup(email,password){
                success,message,userId ->
                if(success){
                    addUserToDatabase(userId,userModel)

                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun addUserToDatabase(userId: String?, userModel: UserModel) {
        authViewModel.addUserToDatabase(userId.toString(),userModel){
            success,message->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
               loadingUtils.dismiss()

            }
        }

    }
}