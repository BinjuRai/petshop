package com.example.mobileapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapplication.R
import com.example.mobileapplication.adapter.FavouriteAdapter
import com.example.mobileapplication.databinding.FragmentFavouriteBinding
import com.example.mobileapplication.repository.fav.FavRepoImpl
import com.example.mobileapplication.viewmodel.FavouriteViewModel

class FavouriteFragment : Fragment() {
    private lateinit var favouriteBinding: FragmentFavouriteBinding
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return favouriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val repo = FavRepoImpl()

        favouriteViewModel = FavouriteViewModel()
        favouriteAdapter = FavouriteAdapter(
            requireContext(), ArrayList(), favouriteViewModel
        )


        }
    }
