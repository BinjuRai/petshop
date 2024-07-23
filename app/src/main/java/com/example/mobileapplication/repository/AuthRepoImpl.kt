package com.example.mobileapplication.repository

import com.example.mobileapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthRepoImpl(var auth: FirebaseAuth) : AuthRepo {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")

    override fun login(username: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Login successful")
            } else {
                callback(false, "Unable",)

            }
        }
    }

    override fun signup(
        username: String,
        password: String,
        callback: (
            Boolean, String?,
            String?
        ) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User registration success", auth.currentUser?.uid)
            } else {
                callback(false, "Unable to register", " ")

            }
        }

    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        userModel.id = userId
        reference.child(userId).setValue(userModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "User registered")
            } else {
                callback(false, "User not registered")
            }

        }
    }

    override fun forgetpassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Reset mail sent to $email",)
            } else {
                callback(false, "Unable to reset password")

            }
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            callback(true, "Logout successful")


        } catch (e: Exception) {
            callback(false, " ")

        }
    }
}