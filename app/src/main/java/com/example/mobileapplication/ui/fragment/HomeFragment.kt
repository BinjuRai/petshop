package com.example.mobileapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplication.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapplication.adapter.ProductAdapter
import com.example.mobileapplication.adapter.user.CategoryUserAdapter
import com.example.mobileapplication.adapter.user.ProductUserAdapter
import com.example.mobileapplication.databinding.FragmentHomeBinding
import com.example.mobileapplication.repository.category.CategoryRepoImpl
import com.example.mobileapplication.repository.product.ProductRepoImpl
import com.example.mobileapplication.viewmodel.CategoryViewModel
import com.example.mobileapplication.viewmodel.ProductViewModel


class HomeFragment : Fragment() {
    lateinit var homeBinding: FragmentHomeBinding
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var productViewModel: ProductViewModel
    lateinit var productUserAdapter: ProductUserAdapter
    lateinit var categoryUserAdapter: CategoryUserAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding=FragmentHomeBinding.inflate(inflater,container,false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var categoryRepo=CategoryRepoImpl()
        categoryViewModel= CategoryViewModel(categoryRepo)
        categoryViewModel.getAllCategory()

        var productRepo=ProductRepoImpl()
        productViewModel=ProductViewModel(productRepo)
        productViewModel.getAllProduct()


        productUserAdapter= ProductUserAdapter(
            requireContext(),
            ArrayList()
        )

        categoryUserAdapter= CategoryUserAdapter(
            requireContext(),
            ArrayList()
        )
        productViewModel.productData.observe(requireActivity()){
            product->
            product?.let{
                productUserAdapter.updateData(it)
            }
        }
        categoryViewModel.categoryData.observe(requireActivity()){
                category->
            category?.let{
                categoryUserAdapter.updateData(it)
            }
        }

        homeBinding.recycleViewCategoryUser.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter=categoryUserAdapter
        }
        homeBinding.recycleViewProductUser.apply {
            layoutManager=GridLayoutManager(requireContext(),2)
            adapter=productUserAdapter
        }

        categoryViewModel.loadingState.observe(requireActivity()){loading->
            if(loading){
                homeBinding.progressbarUserCategory.visibility=View.VISIBLE
            }
            else{
                homeBinding.progressbarUserCategory.visibility=View.GONE
            }
        }
        productViewModel.loadingState.observe(requireActivity()){loading->
            if(loading){
                homeBinding.progressbarUserProduct.visibility=View.VISIBLE
            }
            else{
                homeBinding.progressbarUserProduct.visibility=View.GONE
            }
        }


    }


}