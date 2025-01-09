package com.example.mobileapplication.repository.cart

import android.util.Log
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.model.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartRepoImpl: CartRepo {
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var auth: FirebaseAuth=FirebaseAuth.getInstance()
    var reference : DatabaseReference = database.reference.child("cart")
    override fun addCart(cartModel: CartModel, callback: (Boolean, String?) -> Unit) {
        var id = reference.push().key.toString()
        cartModel.cartid = id

        reference.child(id).setValue(cartModel).addOnCompleteListener { res->
            if(res.isSuccessful){
                callback(true,"Successfully added to cart")
            }else{
                callback(false," Adding Failed")

            }

        }
    }

    override fun deleteCart(cartId: String, callback: (Boolean, String) -> Unit) {
        reference.child(cartId).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Successfully deleted")
            }else{
                callback(false,"Unable to delete data")

            }
        }
    }


    override fun getCart(callback: (List<CartModel>?, Boolean, String?) -> Unit) {
        val currentUser=auth.currentUser
        var userId=currentUser?.uid.toString()

        var query= reference.orderByChild("userId").equalTo(userId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var cartList = mutableListOf<CartModel>()
                for (eachCart in snapshot.children) {

                    var order = eachCart.getValue(CartModel::class.java)

                    Log.d("CHECKPOINTS:::",order?.productName.toString())
                    if (order != null) {
                        cartList.add(order)
                    }
                }
                callback(cartList, true, "Data fetched success")

            }
            override fun onCancelled(error: DatabaseError) {
                callback(null,false,"${error.message}")
            }

        })
    }
}