package com.example.mobileapplication.model

data class CartModel(
    var cartid: String = "",
    var productId: String = "",
    var productPrice: Int = 0,
    var TotalPrice: Int = 0,
    var productImage: String = "",
    var productName: String = "",
    var quantity: Int = 1,
    var userId: String = ""
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", 0, 0, "", "", 1, "")
}
