package com.example.mobileapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return favouriteBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val repo = FavRepoImpl()
        favouriteViewModel = FavouriteViewModel(repo)
        favouriteViewModel.getFavouriteById()

        favouriteAdapter = FavouriteAdapter(requireContext(), ArrayList(), favouriteViewModel)

        favouriteViewModel.favouriteData.observe(viewLifecycleOwner) { favourite ->
            favourite?.let {
                favouriteAdapter.updateData(it)
            }
        }

        favouriteViewModel.loadingState.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                favouriteBinding.progressBarfav.visibility = View.VISIBLE
            } else {
                favouriteBinding.progressBarfav.visibility = View.GONE
            }
        }

        favouriteBinding.recyclerViewFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteAdapter
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = favouriteAdapter.getFavourite(viewHolder.adapterPosition)
                // val imageName = favouriteAdapter.getImageName(viewHolder.adapterPosition)

                favouriteViewModel.deleteFavourite(id) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    // Optional: Handle image deletion
                }
            }
        }).attachToRecyclerView(favouriteBinding.recyclerViewFav)


    }

}