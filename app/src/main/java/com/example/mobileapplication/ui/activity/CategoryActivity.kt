package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mobileapplication.R
import com.example.mobileapplication.adapter.user.ProductUserAdapter
import com.example.mobileapplication.databinding.ActivityCategoryBinding
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.repository.product.ProductRepoImpl
import com.example.mobileapplication.viewmodel.ProductViewModel

class CategoryActivity : AppCompatActivity() {
    lateinit var categoryBinding: ActivityCategoryBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var productUserAdapter: ProductUserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        categoryBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryBinding.root)

        setSupportActionBar(categoryBinding.toolBarCategory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        var repo = ProductRepoImpl()
        productViewModel = ProductViewModel(repo)

        var category: CategoryModel? = intent.getParcelableExtra("category")
        title = category?.categoryName.toString()
        productViewModel.getProductByCategory(category?.categoryName.toString())

        productUserAdapter = ProductUserAdapter(
            this@CategoryActivity, ArrayList()
        )

        productViewModel.productData.observe(this) { product ->
            if(product.isNullOrEmpty()){
                categoryBinding.categoryProductCheck.visibility= View.VISIBLE
                categoryBinding.recycleCategoryProduct.visibility=View.GONE
            }else{
                categoryBinding.categoryProductCheck.visibility= View.GONE
                categoryBinding.recycleCategoryProduct.visibility=View.VISIBLE
                productUserAdapter.updateData(product)
            }

        }
        categoryBinding.recycleCategoryProduct.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            adapter = productUserAdapter


        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }
}
