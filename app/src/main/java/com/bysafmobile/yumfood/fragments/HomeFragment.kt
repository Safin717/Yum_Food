package com.bysafmobile.yumfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bysafmobile.yumfood.activities.MealActivity
import com.bysafmobile.yumfood.adapters.MostPopularAdapter
import com.bysafmobile.yumfood.pojo.Meal
import com.bysafmobile.yumfood.databinding.FragmentHomeBinding
import com.bysafmobile.yumfood.pojo.CategoryMeals
import com.bysafmobile.yumfood.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter

    // для передачи данных в MealActivity создадим константы
    // так как метод putExtra передает данные в виде Ключ - значение
    // а по ключу получаем эти данные в MealActivity
    companion object{
        const val MEAL_ID = "com.bysafmobile.yumfood.fragments.idMeal"
        const val MEAL_NAME = "com.bysafmobile.yumfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.bysafmobile.yumfood.fragments.thumbMeal"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        homeMvvm.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        homeMvvm.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {meal->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rcViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        homeMvvm.observePopularItemsLiveData().observe(viewLifecycleOwner){ mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<CategoryMeals>)
        }
    }

    // при нажатии на randomMealCard с помощью Intent открывается новое активити
    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            // создаем объект Intent с указанием активити, которое нужно открыть
            val intent = Intent(activity, MealActivity::class.java)
            // добавляем в Intent переменную randomMeal
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            // передаем интент
            startActivity(intent)
        }
    }
    // метод следит за обновлениями RandomMeal LiveData
    private fun observerRandomMeal() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            // загружаем картинку RandomMeal во View
            Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }
}