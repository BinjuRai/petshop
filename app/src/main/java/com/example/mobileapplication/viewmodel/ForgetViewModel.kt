package com.example.mobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mobileapplication.repository.ForgetRepo

class ForgetViewModel(var repo: ForgetRepo) : ViewModel() {
    fun forgetpassword(email:String,callback: (Boolean,String?) -> Unit){
        repo.forgetpassword( email, callback)

    }
}