package com.example.mobileapplication.ui.activity.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.adapter.ProductAdapter
import com.example.mobileapplication.databinding.ActivityProductDashboardBinding
import com.example.mobileapplication.repository.product.ProductRepoImpl
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.example.mobileapplication.viewmodel.FavouriteViewModel
import com.example.mobileapplication.viewmodel.ProductViewModel

class ProductDashboardActivity : AppCompatActivity() {
    lateinit var productDashboardBinding: ActivityProductDashboardBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var productViewModel: ProductViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var authViewModel: AuthViewModel
    lateinit var favouriteViewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        productDashboardBinding = ActivityProductDashboardBinding.inflate(layoutInflater)
        setContentView(productDashboardBinding.root)

        // Set up the toolbar
        setSupportActionBar(productDashboardBinding.toolBarDetailProductdetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Product Details"

        // Initialize ViewModels and Utilities
        val repo = ProductRepoImpl()
        productViewModel = ProductViewModel(repo)
        loadingUtils = LoadingUtils(this)

        // Fetch all products from the ViewModel
        productViewModel.getAllProduct()

        // Initialize ProductAdapter with required arguments
        productAdapter = ProductAdapter(
            context = this@ProductDashboardActivity,
            data = ArrayList(),
            loadingUtils = loadingUtils,
            authViewModel = authViewModel,
            favouriteViewModel = favouriteViewModel
        )

        // Observe the product data and update the adapter
        productViewModel.productData.observe(this) { product ->
            product?.let {
                productAdapter.updateData(it)
            }
        }

        // Observe loading state and show progress bar accordingly
        productViewModel.loadingState.observe(this) { loading ->
            if (loading) {
                productDashboardBinding.progressBarDashProduct.visibility = View.VISIBLE
            } else {
                productDashboardBinding.progressBarDashProduct.visibility = View.GONE
            }
        }

        // Set up the RecyclerView with a LinearLayoutManager and ProductAdapter
        productDashboardBinding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductDashboardActivity)
            adapter = productAdapter
        }

        // Implement swipe-to-delete functionality
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Not yet implemented
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = productAdapter.getProductId(viewHolder.adapterPosition)
                val imageName = productAdapter.getImageName(viewHolder.adapterPosition)

                // Delete the product and its image
                productViewModel.deleteProduct(id) { success, message ->
                    if (success) {
                        productViewModel.deleteImage(imageName) { imageSuccess, imageMessage ->
                            if (imageSuccess) {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }).attachToRecyclerView(productDashboardBinding.productRecyclerView)

        // Set up floating action button to add a new product
        productDashboardBinding.productFloating.setOnClickListener {
            val intent = Intent(this@ProductDashboardActivity, AddProductActivity::class.java)
            startActivity(intent)
        }

        // Handle system bar insets to avoid overlapping UI elements
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
