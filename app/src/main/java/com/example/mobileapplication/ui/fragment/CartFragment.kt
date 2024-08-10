package com.example.mobileapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapplication.R
import com.example.mobileapplication.adapter.CartAdapter
import com.example.mobileapplication.databinding.FragmentCartBinding
import com.example.mobileapplication.repository.cart.CartRepoImpl
import com.example.mobileapplication.viewmodel.CartViewModel


class CartFragment : Fragment() {

    lateinit var cartBinding: FragmentCartBinding
    lateinit var cartViewModel: CartViewModel
    lateinit var cartAdapter:CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartBinding = FragmentCartBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return cartBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var repo=CartRepoImpl()

        cartViewModel=CartViewModel(repo)
        cartViewModel.getCartByUserID()

        cartAdapter =CartAdapter(
            requireContext(),ArrayList(),cartViewModel
        )


        cartViewModel.cartData.observe(requireActivity()){
            cart->
            if(cart.isNullOrEmpty()){
                cartBinding.cartCheck.visibility=View.VISIBLE
                cartBinding.recyclerViewCartDash.visibility=View.GONE
            }else{
                cartBinding.cartCheck.visibility=View.GONE
                cartBinding.recyclerViewCartDash.visibility=View.VISIBLE
                cartAdapter.updateData(cart)

            }

        }

        cartViewModel.loadingState.observe(requireActivity()){loading->
            if(loading){
                cartBinding.progressBarCartF.visibility = View.VISIBLE
            }else{
                cartBinding.progressBarCartF.visibility = View.GONE

            }
        }


        cartBinding.recyclerViewCartDash.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=cartAdapter
        }
    }
}