package com.example.mobileapplication.ui.activity.admin

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.adapter.CategoryAdapter
import com.example.mobileapplication.databinding.ActivityAddCategoryAdminBinding
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.repository.category.CategoryRepoImpl
import com.example.mobileapplication.utils.ImageUtils
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.CategoryViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddCategoryAdminActivity : AppCompatActivity() {
    lateinit var addCategoryAdminBinding: ActivityAddCategoryAdminBinding
    lateinit var imageUtils: ImageUtils
    var imageUri: Uri?=null
    lateinit var loadingUtils: LoadingUtils
    lateinit var categoryViewModel: CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addCategoryAdminBinding=ActivityAddCategoryAdminBinding.inflate(layoutInflater)
        setContentView(addCategoryAdminBinding.root)



        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)
        var repo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(repo)


        imageUtils.registerActivity { url ->
            url.let { it ->
                imageUri = it
                Picasso.get().load(it).into(addCategoryAdminBinding.addCategoryimage)
            }
        }
        addCategoryAdminBinding.imguploadadmincategory.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        addCategoryAdminBinding.btnCategoryadd.setOnClickListener {
            if(imageUri == null){
                Toast.makeText(applicationContext,"Please select image first",Toast.LENGTH_SHORT).show()
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
        var imageName = UUID.randomUUID().toString()
        imageUri.let { uri ->
            categoryViewModel.uploadImages(imageName,uri!!){
                    success,url,message->
                if(success){
                    addCategory(url,imageName)
                }else{
                    Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }
    private fun addCategory(url: String?,imageName: String?) {
        loadingUtils.showDialog()
        var categoryName : String = addCategoryAdminBinding.etcategoryname.text.toString()
        var categoryDesc : String = addCategoryAdminBinding.etcateoryDesc.text.toString()

        var data = CategoryModel("",url.toString(),imageName.toString(),categoryName,categoryDesc)

        categoryViewModel.addCategory(data){
                success,message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                finish()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

            }
        }

    }
}