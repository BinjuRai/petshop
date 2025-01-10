package com.example.mobileapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.model.FavModel
import com.example.mobileapplication.model.ProductModel
import com.example.mobileapplication.ui.activity.BuynowActivity
import com.example.mobileapplication.ui.activity.admin.UpdateProductActivity
import com.example.mobileapplication.ui.fragment.FavouriteFragment
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.example.mobileapplication.viewmodel.FavouriteViewModel
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val context: Context,
    private val data: ArrayList<ProductModel>,
    private val loadingUtils: LoadingUtils,
    private val authViewModel: AuthViewModel,
    private val favouriteViewModel: FavouriteViewModel
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.categoryName)
        val productDesc: TextView = view.findViewById(R.id.categorytype)
        val editLabel: TextView = view.findViewById(R.id.categoryEditlabel)
        val heartButton:Button = view.findViewById(R.id.heartButton)
        val imageView: ImageView = view.findViewById(R.id.imageCategory)
        val id: TextView = view.findViewById(R.id.FavProductName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sample_category_admin, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = data[position]

        // Set product details
        holder.productName.text = product.productName
        holder.productDesc.text = product.description

        // Load product image
        Picasso.get().load(product.imageUrl).into(holder.imageView)

        holder.heartButton.setBackgroundResource(R.drawable.likefav)

        // Heart button click listener
        holder.heartButton.setOnClickListener {
            Toast.makeText(context, "Heart button clicked!", Toast.LENGTH_SHORT).show()
            val favouriteModel = FavModel(
                favid = "", // ID generated server-side
                productId = authViewModel.getCurrentUser()?.uid.orEmpty(),
                productName = product.productName.orEmpty(),
                productImage = product.imageUrl.orEmpty()
            )
            addToFavourite(favouriteModel)
            holder.heartButton.backgroundTintList = context.getColorStateList(R.color.Red)
        }

        // Edit label click listener
        holder.editLabel.setOnClickListener {
            val intent = Intent(context, UpdateProductActivity::class.java).apply {
                putExtra("products", product)
            }
            context.startActivity(intent)
        }
    }

    private fun addToFavourite(favModel: FavModel) {
        loadingUtils.showDialog()

        favouriteViewModel.addFavourite(favModel) { success, message ->
            loadingUtils.dismiss()
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            if (success) {
                // Show a toast with the success message
                Toast.makeText(context, "Successfully added to favourite", Toast.LENGTH_LONG).show()
            } else {
                // Show the failure message from the response
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(product: List<ProductModel>) {
        data.clear()
        data.addAll(product)
        notifyDataSetChanged()
    }

    fun getProductId(position: Int): String = data[position].id

    fun getImageName(position: Int): String = data[position].productName
}



