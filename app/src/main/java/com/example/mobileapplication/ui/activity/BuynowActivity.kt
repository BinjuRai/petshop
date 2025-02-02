package com.example.mobileapplication.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapplication.R
import com.example.mobileapplication.databinding.ActivityBuynowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

        // Fetch cart data instead of product data
        fetchCartData { cart ->
            val price = cart["productPrice"].toString().toDoubleOrNull() ?: 0.0
            val quantity = cart["quantity"].toString().toIntOrNull() ?: 1
            val totalPrice = price * quantity  // Multiply price by quantity

            tvProductName.text = cart["productName"].toString()
            tvProductPrice.text = "Total Price: $totalPrice"  // Display multiplied price
            tvProductQuantity.text = "Quantity: $quantity"
            Picasso.get().load(cart["productImage"].toString()).into(ivProductImage)
        }

        fetchUserData { user ->
            tvUserName.text = "Name: ${user["name"].toString()}"
            tvUserAddress.text = "Address: ${user["address"].toString()}"
            tvUserContact.text = "Contact: ${user["phoneNo"].toString()}"
        }

        // Confirm order
        btnConfirmOrder.setOnClickListener {
            Toast.makeText(this, "Order Confirmed!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchCartData(onSuccess: (Map<String, Any>) -> Unit) {
        val cartId = intent.getStringExtra("cartId") ?: return
        Log.d("BuynowActivity", "Fetching cart ID: $cartId")

        database.child("cart").child(cartId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("BuynowActivity", "Cart Snapshot: ${snapshot.value}")
                val cart = snapshot.value as? Map<String, Any>
                if (cart != null) {
                    onSuccess(cart)
                } else {
                    Log.e("BuynowActivity", "Cart data not found")
                    Toast.makeText(this@BuynowActivity, "Cart data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BuynowActivity, "Failed to load cart: ${error.message}", Toast.LENGTH_SHORT).show()
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
