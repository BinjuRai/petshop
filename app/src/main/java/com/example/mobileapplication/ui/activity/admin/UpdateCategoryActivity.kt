package com.example.mobileapplication.ui.activity.admin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityUpdateCategoryBinding
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.repository.category.CategoryRepoImpl
import com.example.mobileapplication.utils.ImageUtils
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.CategoryViewModel
import com.squareup.picasso.Picasso

class UpdateCategoryActivity : AppCompatActivity() {
    lateinit var updateCategoryBinding: ActivityUpdateCategoryBinding
    lateinit var imageUtils: ImageUtils
    var imageUri: Uri?=null
    lateinit var loadingUtils: LoadingUtils
    lateinit var categoryViewModel: CategoryViewModel
    var categoryId =""
    var imageName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateCategoryBinding=ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(updateCategoryBinding.root)

        imageUtils= ImageUtils(this)
        loadingUtils= LoadingUtils(this)
        var repo = CategoryRepoImpl()
        categoryViewModel=CategoryViewModel(repo)

        imageUtils.registerActivity { url->
            url.let{
                imageUri=it
                Picasso.get().load(it).into(updateCategoryBinding.imgupdateadmincategory)
            }
        }

        var category: CategoryModel? = intent.getParcelableExtra("category")
        categoryId=category?.categoryId.toString()

        imageName=category?.categoryImageName.toString()

        updateCategoryBinding.etcategoryupdatename.setText(category?.categoryName)
        updateCategoryBinding.etupdatecateoryDesc.setText(category?.categoryDescriptor)
        Picasso.get().load(category?.categoryImageUrl).into(updateCategoryBinding.updateCategoryimage)

        updateCategoryBinding.btnCategoryupdate.setOnClickListener{
            if(imageUri == null){
                loadingUtils.showDialog()
                updateCategory(category?.categoryImageUrl.toString())
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
    fun uploadImage() {
        loadingUtils.showDialog()
        imageUri.let { uri ->
            categoryViewModel.uploadImages(imageName,uri!!){
                    success,url,message->
                if(success){
                    updateCategory(url)
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun updateCategory(url: String?) {
        var updatedName : String = updateCategoryBinding.etcategoryupdatename.text.toString()
        var updatedDesc : String = updateCategoryBinding.etupdatecateoryDesc.text.toString()

        var updatedMap = mutableMapOf<String,Any?>()

        updatedMap["categoryName"] = updatedName
        updatedMap["categoryDescription"] = updatedDesc
        updatedMap["categoryImageUrl"] = url.toString()

        categoryViewModel.updateCategory(categoryId,updatedMap){
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