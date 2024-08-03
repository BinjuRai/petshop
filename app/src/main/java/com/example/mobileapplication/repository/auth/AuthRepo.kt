package com.example.mobileapplication.repository.auth

import android.net.Uri
import com.example.mobileapplication.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    //For Authentication
        fun login(username: String,password:String,callback:(Boolean,String?)->Unit)
        fun signup(username: String,password:String,callback:(Boolean,String?,String?)->Unit)

     //For Realtime database
        fun addUserToDatabase(userId:String,userModel: UserModel,callback: (Boolean, String?) -> Unit)

     //Forget password
        fun forgetpassword(email:String,callback: (Boolean, String?) -> Unit)

        fun getCurrentUser():FirebaseUser?


        fun getUserFromFirebase(userId: String,callback: (UserModel?,Boolean, String?) -> Unit)
     //For logOut
        fun logout(callback: (Boolean, String?) -> Unit)

        fun uploadImage(imageName:String, imageUri: Uri, callback: (Boolean, String?,String?) -> Unit)

    fun updateUser(userID:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit)
    }
