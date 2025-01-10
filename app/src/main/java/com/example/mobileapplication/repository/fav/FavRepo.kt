package com.example.mobileapplication.repository.fav

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.model.FavModel

interface FavRepo {

    fun addFavourite(favoriteModel: FavModel, callback: (Boolean, String?) -> Unit)

    fun deleteFavourite(favouriteid: String, callback: (Boolean, String?) -> Unit)

    fun getFavourite(callback: (List<FavModel>?,Boolean, String?) -> Unit)

    fun updateFavourite(favouriteid:String,data: MutableMap<String,Any?>,callback: (Boolean, String?) -> Unit)
}
