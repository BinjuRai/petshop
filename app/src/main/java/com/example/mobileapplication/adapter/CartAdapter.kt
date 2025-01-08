package com.example.mobileapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.model.ProductModel
import com.example.mobileapplication.viewmodel.CartViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CartAdapter(var context: Context,var data:ArrayList<CartModel>,var cartViewModel: CartViewModel):RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        var cartId: TextView = view.findViewById(R.id.IDCart)
        var productName: TextView = view.findViewById(R.id.CartProductName)

        var TotalPrice: TextView = view.findViewById(R.id.CartProductPrice)

        var quantity:TextView=view.findViewById(R.id.CartProductQuantity)

        var imageView: ImageView = view.findViewById(R.id.imgViewCartSample)

        var progressBar:ProgressBar=view.findViewById(R.id.progressBarcartSample)

        var removeButton: TextView = view.findViewById(R.id.CartRemoveText)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.sample_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.cartId.text = "Cart ID:" + data[position].cartid
        holder.productName.text = data[position].productName
        holder.TotalPrice.text = "RS." + (data[position].productPrice* data[position].quantity).toString()
        holder.quantity.text = "Qty:" + data[position].quantity.toString()


        var image = data[position].productImage
        Picasso.get().load(image).into(holder.imageView, object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE

            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, e?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        holder.removeButton.setOnClickListener {
            cartViewModel.deleteCart(data[position].cartid) { success, message ->
                if (success) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    @SuppressLint("NotifyDataSetChanged")

    fun updateData(cart: List<CartModel>) {
        data.clear()
        data.addAll(cart)
        notifyDataSetChanged()
    }
}