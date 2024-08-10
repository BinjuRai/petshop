package com.example.mobileapplication.repository

import com.google.firebase.auth.FirebaseAuth

class ForegetRepoImpl(var auth: FirebaseAuth) : ForgetRepo {
    override fun forgetpassword(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Reset mail sent to $email",)
            } else {
                callback(false, "Unable to reset password")

            }
        }
    }
}