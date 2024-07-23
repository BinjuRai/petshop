package com.example.mobileapplication.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.mobileapplication.R

class LoadingUtils(var activity: Activity) {
    lateinit var alertDialog: AlertDialog

    fun showDialog(){
        var dialogView=activity.layoutInflater.inflate(R.layout.loading_dialog,null)
        var builder=AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog=builder.create()
        alertDialog.show()
    }
    fun dismiss(){
        alertDialog.dismiss()

    }
}