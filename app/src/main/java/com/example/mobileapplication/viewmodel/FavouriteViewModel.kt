package com.example.mobileapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobileapplication.model.FavModel
import com.example.mobileapplication.repository.fav.FavRepo

class FavouriteViewModel (var repo:FavRepo):ViewModel(){
    fun addFavourite(favoriteModel: FavModel, callback: (Boolean, String?) -> Unit){
    repo.addFavourite(favoriteModel,callback)
}

    fun deleteFavourite(favouriteId: String, callback: (Boolean, String?) -> Unit){
        repo.deleteFavourite(favouriteId,callback)
    }

    private var _favouriteData = MutableLiveData<List<FavModel>?>()
    var favouriteData = MutableLiveData<List<FavModel>?>()
        get() = _favouriteData


    private var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState
    fun getFavouriteById() {
        _loadingState.value = true
        repo.getFavourite{
                favouriteList,success,message->
            if(favouriteList!=null){
                _loadingState.value = false
                _favouriteData.value = favouriteList
            }
        }
    }
}