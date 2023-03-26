package com.bysafmobile.yumfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bysafmobile.yumfood.activities.MainActivity
import com.bysafmobile.yumfood.adapters.MealsAdapter
import com.bysafmobile.yumfood.databinding.FragmentFavouritesBinding
import com.bysafmobile.yumfood.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment() {
    private lateinit var binding:FragmentFavouritesBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var favouritesAdapter:MealsAdapter
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

        // добавим функцию swipe-to-dismiss в RecyclerView.
        // Этот класс является подклассом RecyclerView.ItemDecoration,
        // благодаря чему её легко добавить к LayoutManager и адаптеру.
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favouritesAdapter.differ.currentList[position]
                viewModel.deleteMeal(deletedMeal)
                Snackbar.make(requireView(), "Meal Deleted ", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.insertMeal(deletedMeal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rcFavourites)
    }

    private fun prepareRecyclerViewFavourites() {
        favouritesAdapter = MealsAdapter()
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