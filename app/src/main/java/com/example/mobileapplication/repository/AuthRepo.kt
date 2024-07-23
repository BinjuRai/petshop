package com.example.mobileapplication.repository

import com.example.mobileapplication.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    //For Authentication
        fun login(username: String,password:String,callback:(Boolean,String)->Unit)
        fun signup(username: String,password:String,callback:(Boolean,String?,String?)->Unit)

     //For Realtime database
        fun addUserToDatabase(userId:String,userModel: UserModel,callback: (Boolean, String) -> Unit)

     //Forget password
        fun forgetpassword(email:String,callback: (Boolean, String) -> Unit)

        fun getCurrentUser():FirebaseUser?


     //For logOut
        fun logout(callback: (Boolean, String) -> Unit)
    }
