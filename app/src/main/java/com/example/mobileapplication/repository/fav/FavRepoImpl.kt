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
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference = firebaseDatabase.reference.child("favourite")
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun addFavourite(favoriteModel: FavModel, callback: (Boolean, String?) -> Unit) {
        var id = ref.push().key ?: ""
        favoriteModel.favid = id

        ref.child(id).setValue(favoriteModel).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Added to Favourite")
            } else {
                callback(false, "Unable to add")
            }
        }
    }

    override fun deleteFavourite(favouriteid: String, callback: (Boolean, String?) -> Unit) {
        ref.child(favouriteid).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "deleted successfully")
            } else {
                callback(false, "Unable to delete")
            }
        }
    }

    override fun getFavourite(callback: (List<FavModel>?, Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid.toString()

        val query = ref.orderByChild("userId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var favouriteList = mutableListOf<FavModel>()
                for (eachFavourite in snapshot.children) {
                    var favourite = eachFavourite.getValue(FavModel::class.java)
                    if(favourite != null){
                        favouriteList.add(favourite)
                        Log.d("checkpoint",favourite?.favid.toString())
                    }

                }
                callback(favouriteList,true,"Data fetched")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false, "${error.message}")
            }
        })
    }

    override fun updateFavourite(
        favouriteid: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String?) -> Unit
    ) {
        data.let { it->
            ref.child(favouriteid).updateChildren(it).addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Successfully updated")
                }else{
                    callback(false,"Unable to update data")

                }
            }
        }
    }

}
