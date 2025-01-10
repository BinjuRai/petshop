package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityBuynowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class BuynowActivity : AppCompatActivity() {

    lateinit var buynowBinding: ActivityBuynowBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        buynowBinding = ActivityBuynowBinding.inflate(layoutInflater)
        setContentView(buynowBinding.root)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Views
        val tvProductName: TextView = findViewById(R.id.tvProductName)
        val tvProductPrice: TextView = findViewById(R.id.tvProductPrice)
        val tvProductQuantity: TextView = findViewById(R.id.tvProductQuantity)
        val ivProductImage: ImageView = findViewById(R.id.ivProductImage)

        val tvUserName: TextView = findViewById(R.id.tvUserName)
        val tvUserAddress: TextView = findViewById(R.id.tvUserAddress)
        val tvUserContact: TextView = findViewById(R.id.tvUserContact)

        val btnConfirmOrder: Button = findViewById(R.id.btnConfirmOrder)

        // Fetch product and user data
        fetchProductData { product ->
            tvProductName.text = product["name"].toString()
            tvProductPrice.text = "Price: ${product["price"].toString()}"
            tvProductQuantity.text = "Quantity: ${product["quantity"].toString()}"
            Picasso.get().load(product["image"].toString()).into(ivProductImage)
        }

        fetchUserData { user ->
            tvUserName.text = "Name: ${user["name"].toString()}"
            tvUserAddress.text = "Address: ${user["address"].toString()}"
            tvUserContact.text = "Contact: ${user["contact"].toString()}"
        }

        // Confirm order
        btnConfirmOrder.setOnClickListener {
            Toast.makeText(this, "Order Confirmed!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchProductData(onSuccess: (Map<String, Any>) -> Unit) {
        val productId = intent.getStringExtra("productId") ?: return
        database.child("products").child(productId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.value as? Map<String, Any>
                if (product != null) {
                    onSuccess(product)
                } else {
                    Toast.makeText(this@BuynowActivity, "Product not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BuynowActivity, "Failed to load product: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserData(onSuccess: (Map<String, Any>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        database.child("users").child(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.value as? Map<String, Any>
                if (user != null) {
                    onSuccess(user)
                } else {
                    Toast.makeText(this@BuynowActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BuynowActivity, "Failed to load user: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}