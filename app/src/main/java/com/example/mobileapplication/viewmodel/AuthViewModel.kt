package com.example.mobileapplication.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapplication.model.UserModel
import com.example.mobileapplication.repository.auth.AuthRepo
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(var repo: AuthRepo):ViewModel() {
    fun login(username: String,password:String,callback:(Boolean,String)->Unit){
         repo.login(username,password, callback)
    }
    fun signup(username: String,password:String,callback:(Boolean,String?,String?)->Unit){
        repo.signup(username, password, callback)

    }
    fun addUserToDatabase(userId:String, userModel: UserModel, callback: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId, userModel, callback)

    }
    fun getCurrentUser(): FirebaseUser? {
       return repo.getCurrentUser()
    }
    fun forgetpassword(email:String,callback: (Boolean,String) -> Unit){
        repo.forgetpassword( email, callback)

    }
    fun logout(callback: (Boolean, String) -> Unit){
        repo.logout(callback)
    }

    private var _userData = MutableLiveData<UserModel?>()
    var userData =MutableLiveData<UserModel?>()
        get()=_userData
    fun fetchUserData(userId:String){
        repo.getUserFromFirebase(userId){
            userModel,success ,message->
                if (success) {
                    _userData.value = userModel
                }

        }
    }

    fun uploadImage(imageName:String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit){
        repo.uploadImage(imageName,imageUri,callback)

    }

    fun updateUser(userID:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit){
        repo.updateUser(userID,data,callback)

    }

}