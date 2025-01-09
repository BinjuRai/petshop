package com.example.mobileapplication.repository.fav

import android.util.Log
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.model.FavModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavRepoImpl : FavRepo {
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var reference: DatabaseReference = database.reference.child("favourites")
    override fun id(imageName: String) {
        TODO("Not yet implemented")
    }

    override fun addFavouriteModel(favModel: FavModel, callback: (Boolean, String?) -> Unit) {
        val id = reference.push().key.toString()
        favModel.productId = id

        reference.child(id).setValue(favModel).addOnCompleteListener { res ->
            if (res.isSuccessful) {
                callback(true, "Successfully added to favourites")
            } else {
                callback(false, "Adding to favourites failed")
            }
        }
    }

    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {

    }

    override fun updateFavourite(callback: (List<FavModel>?, Boolean, String?) -> Unit) {

    }


}
