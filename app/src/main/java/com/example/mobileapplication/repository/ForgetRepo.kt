package com.example.mobileapplication.repository

interface ForgetRepo {
    fun forgetpassword(email:String,callback: (Boolean, String?) -> Unit)

}