package com.example.mobileapplication.repository.fav

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.model.FavModel

interface FavRepo {

    fun id(imageName: String)

    fun addFavouriteModel (favModel: FavModel, callback: (Boolean, String?) -> Unit)

    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit)

    fun updateFavourite(callback: (List<FavModel>?, Boolean, String?) -> Unit)
}
