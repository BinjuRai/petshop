package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityProductDetailBinding
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.model.ProductModel
import com.example.mobileapplication.repository.auth.AuthRepoImpl
import com.example.mobileapplication.repository.cart.CartRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.example.mobileapplication.viewmodel.CartViewModel
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {
    lateinit var productDetailBinding: ActivityProductDetailBinding
    lateinit var authViewModel: AuthViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var loadingUtils: LoadingUtils
    var quantity:Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        productDetailBinding=ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(productDetailBinding.root)

       setSupportActionBar(productDetailBinding.toolBarDetail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Furry's Details"

        var cartrepo =CartRepoImpl()
        cartViewModel= CartViewModel(cartrepo)

        var authrepo =AuthRepoImpl()
        authViewModel= AuthViewModel(authrepo)

        loadingUtils= LoadingUtils(this)

        var products: ProductModel? =intent.getParcelableExtra("products")
        productDetailBinding.imgNameDetail.text=products?.productName
        productDetailBinding.imgDescdetail.text=products?.description
        productDetailBinding.imgPricedetail.text=products?.price.toString()

        Picasso.get().load(products?.imageUrl).into(productDetailBinding.imgViewDetail)

        productDetailBinding.btnAdd.setOnClickListener{
            if(quantity<5){
                quantity++
                productDetailBinding.quantityDetail.text=quantity.toString()
            }
        }
        productDetailBinding.btnSubtr.setOnClickListener{
            if(quantity>1){
                quantity--
                productDetailBinding.quantityDetail.text=quantity.toString()
            }

        }

        productDetailBinding.btnAddToCart.setOnClickListener{
            addToCart(products)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addToCart(products: ProductModel?) {
        loadingUtils.showDialog()
        var currentUser= authViewModel.getCurrentUser()
        var cartModel=CartModel("",products?.id.toString(),
            products?.price.toString().toInt(),
            products?.imageUrl.toString(),
            products?.productName.toString(),
            quantity,
            currentUser?.uid.toString())

        cartViewModel.addCart(cartModel){
                success,message->
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



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                finish()
            true
        }
            else -> super.onOptionsItemSelected(item)
        }
    }
}