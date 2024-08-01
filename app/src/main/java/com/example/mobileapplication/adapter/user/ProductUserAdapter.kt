package com.example.mobileapplication.adapter.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.model.CategoryModel
import com.example.mobileapplication.model.ProductModel
import com.example.mobileapplication.ui.activity.ProductDetailActivity
import com.example.mobileapplication.ui.activity.admin.UpdateProductActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProductUserAdapter (var context: Context, var data : ArrayList<ProductModel>) : RecyclerView.Adapter<ProductUserAdapter.ProductUserViewHolder>() {

    class ProductUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productname: TextView = view.findViewById(R.id.productnameUser)
        var productprice: TextView = view.findViewById(R.id.productPriceUser)
        var progressBar: ProgressBar =view.findViewById(R.id.pbeachgProduct)
        var card:CardView=view.findViewById(R.id.CardProductUser)
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


    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(product: List<ProductModel>){
        data.clear()
        data.addAll(product)
        notifyDataSetChanged()
    }


}
