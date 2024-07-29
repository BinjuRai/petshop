package com.example.mobileapplication.model

import android.location.Address
import android.os.Parcel
import android.os.Parcelable

data class UserModel (
    var id :String=" ",
    var  name:String=" ",
    var phoneno :String = "",
    var  address:String=" ",
    var email:String =" ",
    var imageName :String="",
    var imageUrl:String=""

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(phoneno)
        parcel.writeString(address)
        parcel.writeString(email)
        parcel.writeString(imageName)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

}


}

