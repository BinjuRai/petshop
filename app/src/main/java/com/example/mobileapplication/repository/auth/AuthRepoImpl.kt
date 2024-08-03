package com.example.mobileapplication.repository.auth

import android.net.Uri
import com.example.mobileapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AuthRepoImpl : AuthRepo {
    var auth : FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var reference: DatabaseReference = database.reference.child("users")

    var storage : FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = storage.reference.child("users")

    override fun login(username: String, password: String, callback: (Boolean, String?) -> Unit) {
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
        callback: (Boolean, String?) -> Unit
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

    override fun forgetpassword(email: String, callback: (Boolean, String?) -> Unit) {
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

    override fun getUserFromFirebase(
        userId: String,
        callback: (UserModel?, Boolean, String?) -> Unit
    ) {
        reference.child(userId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val userModel=snapshot.getValue(UserModel::class.java)
                    callback(userModel,true,"Fetch Success")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }

    override fun logout(callback: (Boolean, String?) -> Unit) {
        try {
            auth.signOut()
            callback(true, "Logout successful")


        } catch (e: Exception) {
            callback(false, " ")

        }
    }

    override fun uploadImage(
        imageName: String,
        imageUri: Uri,
        callback: (Boolean, String?, String?) -> Unit
    ) {
        var imageReference = storageRef.child(imageName)
            imageUri.let {url->
                imageReference.putFile(url).addOnSuccessListener {
                    imageReference.downloadUrl.addOnSuccessListener {it->
                        var imageUrl = it.toString()
                        callback(true,imageUrl,"Upload success")
                    }
                }.addOnFailureListener{
                    callback(false,"","unable to upload")
                }
            }
    }

    override fun updateUser(
        userID: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String?) -> Unit
    ) {
        data.let { it->
            reference.child(userID).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Successfully updated")
                }else{
                    callback(false,"Unable to update data")

                }
            }
        }
    }
}



