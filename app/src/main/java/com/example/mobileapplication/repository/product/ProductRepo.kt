package com.example.mobileapplication.repository.product

import android.net.Uri
import com.example.mobileapplication.model.ProductModel

interface ProductRepo {
    fun uploadImages(imageName: String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit)

    fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit)

    fun updateProduct(productId:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit)

    fun deleteProduct(productId:String,callback: (Boolean, String?) -> Unit)

    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)

    fun getAllProduct(callback: (List<ProductModel>?, Boolean, String?) -> Unit)
}