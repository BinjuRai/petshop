package com.example.mobileapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import com.example.mobileapplication.model.CartModel
import com.example.mobileapplication.ui.activity.BuynowActivity
import com.example.mobileapplication.viewmodel.CartViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CartAdapter(
    private val context: Context,
    private val data: ArrayList<CartModel>,
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cartId: TextView = view.findViewById(R.id.IDCart)
        val buyNow: Button = view.findViewById(R.id.buynowbtn)
        val productName: TextView = view.findViewById(R.id.CartProductName)
        val totalPrice: TextView = view.findViewById(R.id.CartProductPrice)
        val quantity: TextView = view.findViewById(R.id.CartProductQuantity)
        val imageView: ImageView = view.findViewById(R.id.imgViewCartSample)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarcartSample)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = data[position]

        holder.buyNow.setOnClickListener {
            Log.d("CartFragment", "Buy Now button clicked")
            var intent = Intent(context, BuynowActivity::class.java)

            // Add data to the intent using a bundle
            intent.putExtra("pName", cartItem.productName) // Example data
            intent.putExtra("productId", cartItem.productId) // Example data
            intent.putExtra("pPrice", cartItem.productPrice.toString()) // Example data
            intent.putExtra("Quantity", cartItem.quantity) // Example data

            context.startActivity(intent)
        }
        holder.cartId.text = "Cart ID: ${cartItem.cartid}"
        holder.productName.text = cartItem.productName
        holder.totalPrice.text = "RS. ${cartItem.productPrice * cartItem.quantity}"
        holder.quantity.text = "Qty: ${cartItem.quantity}"

        Picasso.get().load(cartItem.productImage).into(holder.imageView, object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, e?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(cart: List<CartModel>) {
        data.clear()
        data.addAll(cart)
        notifyDataSetChanged()
    }

    // Method to get the product ID of a cart item
    fun getCartId(position: Int): String {
        return data[position].cartid
    }
}