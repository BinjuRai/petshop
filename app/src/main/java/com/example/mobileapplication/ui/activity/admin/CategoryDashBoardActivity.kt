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
import com.example.mobileapplication.adapter.CategoryAdapter
import com.example.mobileapplication.databinding.ActivityCategoryDashBoardBinding
import com.example.mobileapplication.repository.category.CategoryRepoImpl
import com.example.mobileapplication.viewmodel.CategoryViewModel

class CategoryDashBoardActivity : AppCompatActivity() {
    lateinit var categoryDashBoardBinding: ActivityCategoryDashBoardBinding
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoryViewModel: CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        categoryDashBoardBinding = ActivityCategoryDashBoardBinding.inflate(layoutInflater)
        setContentView(categoryDashBoardBinding.root)

        var repo = CategoryRepoImpl()
        categoryViewModel = CategoryViewModel(repo)

        categoryViewModel.getAllCategory()
        categoryAdapter = CategoryAdapter(this@CategoryDashBoardActivity, ArrayList())

        categoryViewModel.categoryData.observe(this) { category ->
            category?.let {
                categoryAdapter.updateData(it)
            }
        }

        categoryViewModel.loadingState.observe(this) { loading ->
            if (loading) {
                categoryDashBoardBinding.progressBarCategoryDash.visibility = View.VISIBLE
            } else {
                categoryDashBoardBinding.progressBarCategoryDash.visibility = View.GONE
            }
        }

        categoryDashBoardBinding.categoryRecycleView.apply {
            layoutManager = LinearLayoutManager(this@CategoryDashBoardActivity)
            adapter = categoryAdapter
        }

        categoryDashBoardBinding.floatingbtncategorydashboard.setOnClickListener {
            var intent =
                Intent(this@CategoryDashBoardActivity, AddCategoryAdminActivity::class.java)
            startActivity(intent)
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = categoryAdapter.getCategoryId(viewHolder.adapterPosition)
                var imageName = categoryAdapter.getImageName(viewHolder.adapterPosition)

                categoryViewModel.deleteCategory(id) { success, message ->
                    if (success) {
                        categoryViewModel.deleteImage(imageName) { s, m ->
                            if (s) {
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    } else {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }).attachToRecyclerView(categoryDashBoardBinding.categoryRecycleView)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}