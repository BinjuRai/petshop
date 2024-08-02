package com.example.mobileapplication.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.repository.cart.CartRepo

class CartViewModel(var repo : CartRepo): ViewModel() {
    fun addCart(cartModel: CartModel, callback: (Boolean, String?)->Unit){
        repo.addCart(cartModel, callback)
    }
    fun deleteCart(cartId:String,callback: (Boolean, String?) -> Unit){
        repo.deleteCart(cartId,callback)
    }
    private var _cartData = MutableLiveData<List<CartModel>?>()
    var cartData = MutableLiveData<List<CartModel>?>()
        get() = _cartData


    private var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun getCartByUserID(){
        _loadingState.value = true
        repo.getCart{
                cartList,success,message->
            if(cartList!=null){
                _loadingState.value = false
                _cartData.value = cartList
            }
        }
    }
}