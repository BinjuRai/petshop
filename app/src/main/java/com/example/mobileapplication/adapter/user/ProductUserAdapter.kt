package com.example.mobileapplication.adapter.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.model.FavModel
import com.example.mobileapplication.model.ProductModel
import com.example.mobileapplication.ui.activity.ProductDetailActivity
import com.example.mobileapplication.ui.activity.admin.UpdateProductActivity
import com.example.mobileapplication.utils.LoadingUtils
import com.example.mobileapplication.viewmodel.AuthViewModel
import com.example.mobileapplication.viewmodel.FavouriteViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductUserAdapter (var context: Context, var data : ArrayList<ProductModel>,
                          private val authViewModel: AuthViewModel,
                          private val favouriteViewModel: FavouriteViewModel,
                          private val loadingUtils: LoadingUtils

) : RecyclerView.Adapter<ProductUserAdapter.ProductUserViewHolder>() {

    class ProductUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productname: TextView = view.findViewById(R.id.productnameUser)
        var productprice: TextView = view.findViewById(R.id.productPriceUser)
        var card: CardView = view.findViewById(R.id.CardProductUser)
        val heartButton: Button = view.findViewById(R.id.heartButtonP)
        val progressBar: ProgressBar = view.findViewById(R.id.pbeachgProduct)
        var imageView: ImageView = view.findViewById(R.id.productImgUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductUserViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sample_product_user, parent, false)
        return ProductUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductUserViewHolder, position: Int) {
        holder.productname.text = data[position].productName
        holder.productprice.text="Rs.${data[position].price.toString()}"

        var image = data[position].imageUrl
        Picasso.get().load(image).into(holder.imageView,object :Callback{
            override fun onSuccess() {
                holder.progressBar.visibility=View.GONE

            }

            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        holder.card.setOnClickListener{
            var intent =Intent(context,ProductDetailActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }
        holder.heartButton.setBackgroundResource(R.drawable.likefav)

        // Heart button click listener
        holder.heartButton.setOnClickListener {
            Toast.makeText(context, "Added to Favourite!", Toast.LENGTH_SHORT).show()
            val favouriteModel = FavModel(
                favid = "", // ID generated server-side
                userId = authViewModel.getCurrentUser()?.uid.orEmpty(),
                productId = data[position].id,
                productImage = data[position].imageUrl,
                productName = data[position].productName,

            )
            addToFavourite(favouriteModel)
            holder.heartButton.backgroundTintList = context.getColorStateList(R.color.Red)
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
    fun updateData(product: List<ProductModel>){
        data.clear()
        data.addAll(product)
        notifyDataSetChanged()
    }



}
