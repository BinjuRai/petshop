package com.example.mobileapplication.adapter

import android.annotation.SuppressLint
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
import com.example.mobileapplication.model.FavModel
import com.example.mobileapplication.ui.activity.BuynowActivity
import com.example.mobileapplication.ui.fragment.FavouriteFragment
import com.example.mobileapplication.viewmodel.CartViewModel
import com.example.mobileapplication.viewmodel.FavouriteViewModel
import com.google.firebase.database.core.Context
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FavouriteAdapter(
    var context: android.content.Context,
    var data:ArrayList<FavModel>,
    var FavouriteViewModel: FavouriteViewModel
):RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>(){

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productName: TextView = view.findViewById(R.id.FavProductName)
        var imageView: ImageView = view.findViewById(R.id.imgViewfavSample)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBarFavSample)
        var heartButton: Button= view.findViewById(R.id.heartButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_favourite, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.productName.text = data[position].productName
        holder.heartButton.setOnClickListener {
            Log.d("FavFragment", "Favourite button clicked")
            var intent = Intent(context, FavouriteFragment::class.java)

            context.startActivity(intent)
        }


        val image = data[position].productImage
        Picasso.get().load(image).into(holder.imageView, object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, e?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })


        }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(favourite: List<FavModel>){
        data.clear()
        data.addAll(favourite)
        notifyDataSetChanged()
    }
    fun getFavourite(position: Int) : String{
        return data[position].favid
    }
    }


