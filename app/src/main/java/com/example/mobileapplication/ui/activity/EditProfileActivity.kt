package com.example.mobileapplication.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityEditProfileBinding
import com.example.mobileapplication.databinding.FragmentProfileBinding
import com.example.mobileapplication.model.UserModel
import com.example.mobileapplication.repository.AuthRepo
import com.example.mobileapplication.repository.AuthRepoImpl
import com.example.mobileapplication.utils.ImageUtils
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class EditProfileActivity : AppCompatActivity() {
lateinit var editProfileBinding: ActivityEditProfileBinding
lateinit var imageUtils: ImageUtils
    lateinit var loadingUtils: LoadingUtils
var imageUri: Uri?= null
    var imageName=""
    var userid=""
    var oldImageUrl = ""
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        editProfileBinding=ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(editProfileBinding.root)

        var repo=AuthRepoImpl()
        authViewModel=AuthViewModel(repo)

        imageUtils= ImageUtils(this)
        imageUtils.registerActivity { url->
            url.let{it->
                imageUri=it
                Picasso.get().load(it).into(editProfileBinding.innerUimage)
            }
        }


        var userData :UserModel? = intent.getParcelableExtra("userData")
        userData.let{users->
            userid=users?.id.toString()
            imageName=if(imageName==null||imageName!!.isEmpty()){
                UUID.randomUUID().toString()
            }else{
                users?.imageName.toString()
            }
            oldImageUrl = users?.imageUrl.toString()
            editProfileBinding.editUFullName.setText(users?.name)
            editProfileBinding.editUPhoneNo .setText(users?.phoneno)
            editProfileBinding.editUAddress.setText(users?.address)
            if(users!!.imageUrl==null || users.imageUrl.isEmpty()){
                editProfileBinding.innerUimage.setImageResource(R.drawable.dummyprofilepic)
            }else{
                Picasso.get().load(users.imageUrl).into(editProfileBinding.innerUimage)
            }
        }

        editProfileBinding.browseGallery.setOnClickListener{
            imageUtils.launchGallery(this)
        }

        editProfileBinding.Upbtn.setOnClickListener {
            if(imageUri == null){
                updateProduct(oldImageUrl)
            }else{

                uploadImage()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage(){
        loadingUtils.showDialog()
        imageUri.let{uri->
            authViewModel.uploadImage(imageName,uri!!){
                success,uri,message->
                if(success){
                    updateProduct(uri)

                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }

        }
    }

    private fun updateProduct(url: String?) {
        loadingUtils.showDialog()
        var updatedName:String =editProfileBinding.editUFullName.text.toString()
        var updatedPhoneNo:String =editProfileBinding.editUPhoneNo.text.toString()
        var updatedAddress:String =editProfileBinding.editUAddress.text.toString()

        var updatedMap= mutableMapOf<String,Any?>()

        updatedMap["name"]=updatedName
        updatedMap["phoneNo"]=updatedPhoneNo
        updatedMap["address"]=updatedAddress
        updatedMap["imageName"]=imageName.toString()
        updatedMap["imageUrl"]= url.toString()

        authViewModel.updateUser(userid,updatedMap){
                success,message->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
            }
        }
    }



}