package com.bysafmobile.yumfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bysafmobile.yumfood.R
import com.bysafmobile.yumfood.activities.MainActivity
import com.bysafmobile.yumfood.adapters.FavouritesMealsAdapter
import com.bysafmobile.yumfood.databinding.FragmentFavouritesBinding
import com.bysafmobile.yumfood.viewmodel.HomeViewModel


class FavouritesFragment : Fragment() {
    private lateinit var binding:FragmentFavouritesBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var favouritesAdapter:FavouritesMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerViewFavourites()
        observerFavouritesMeals()
    }

    private fun prepareRecyclerViewFavourites() {
        favouritesAdapter = FavouritesMealsAdapter()
        binding.rcFavourites.apply {
            layoutManager = GridLayoutManager(context, 2,GridLayoutManager.VERTICAL, false )
            adapter = favouritesAdapter
        }
    }

    private fun observerFavouritesMeals() {
        viewModel.observeFavouritesMealsLiveData().observe(viewLifecycleOwner, Observer {meals->
            favouritesAdapter.differ.submitList(meals)
        })
    }
}