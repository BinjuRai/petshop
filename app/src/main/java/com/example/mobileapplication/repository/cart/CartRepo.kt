package com.example.mobileapplication.repository.cart

import com.example.mobileapplication.model.CartModel
import javax.security.auth.callback.Callback

interface CartRepo {
    fun addCart(cartModel: CartModel,callback: (Boolean,String?)->Unit)
    fun deleteCart(cartId: String, callback: (Boolean, String) -> Unit)
    fun getCart(callback: (List<CartModel>?,Boolean, String?) -> Unit)
}