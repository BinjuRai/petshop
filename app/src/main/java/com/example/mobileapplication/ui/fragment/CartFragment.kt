package com.example.mobileapplication.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.adapter.CartAdapter
import com.example.mobileapplication.databinding.FragmentCartBinding
import com.example.mobileapplication.databinding.SampleCartBinding
import com.example.mobileapplication.repository.cart.CartRepoImpl
import com.example.mobileapplication.ui.activity.BuynowActivity
import com.example.mobileapplication.ui.activity.admin.CategoryDashBoardActivity
import com.example.mobileapplication.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private lateinit var cartBinding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    lateinit var sampleCartBinding: SampleCartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartBinding = FragmentCartBinding.inflate(inflater, container, false)

        sampleCartBinding = SampleCartBinding.inflate(inflater, container, false)
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repository and ViewModel
        val repo = CartRepoImpl()
        cartViewModel = CartViewModel(repo)

        // Fetch cart data
        cartViewModel.getCartByUserID()

        // Initialize adapter with empty data
        cartAdapter = CartAdapter(requireContext(), ArrayList(), cartViewModel)


        // Observe cart data and update UI
        cartViewModel.cartData.observe(viewLifecycleOwner) { cart ->
            if (cart.isNullOrEmpty()) {
                cartBinding.cartCheck.visibility = View.VISIBLE
                cartBinding.recyclerViewCartDash.visibility = View.GONE
            } else {
                cartBinding.cartCheck.visibility = View.GONE
                cartBinding.recyclerViewCartDash.visibility = View.VISIBLE
                cartAdapter.updateData(cart)
            }
        }

        // Observe loading state
        cartViewModel.loadingState.observe(viewLifecycleOwner) { loading ->
            cartBinding.progressBarCartF.visibility = if (loading) View.VISIBLE else View.GONE
        }

        // Set up RecyclerView
        cartBinding.recyclerViewCartDash.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Attach swipe-to-delete functionality
        attachSwipeToDelete()
    }

    private fun attachSwipeToDelete() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // No movement supported
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Get the cart item to delete
                val position = viewHolder.adapterPosition
                val cartId = cartAdapter.getCartId(position) // Retrieve cart ID

                // Delete cart item from database
                cartViewModel.deleteCart(cartId) { success, message ->
                    if (success) {
                        Toast.makeText(requireContext(), "Item deleted successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }

                // Update adapter to reflect item removal
                cartAdapter.notifyItemRemoved(position)
            }
        })

        // Attach ItemTouchHelper to RecyclerView
        itemTouchHelper.attachToRecyclerView(cartBinding.recyclerViewCartDash)
    }
}
