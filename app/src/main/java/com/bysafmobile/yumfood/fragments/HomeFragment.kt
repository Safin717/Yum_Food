package com.bysafmobile.yumfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bysafmobile.yumfood.R
import com.bysafmobile.yumfood.data.Meal
import com.bysafmobile.yumfood.data.MealList
import com.bysafmobile.yumfood.databinding.FragmentHomeBinding
import com.bysafmobile.yumfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList>{
            // Retrofit подключен к API
            // получаем информацию о случайной еде
            // и показываем его в imageView
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                //если response not null
                if(response.body() != null){
                    // переменная randomMeal принимает из response нулевой элемент
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d("Test", "meal id ${randomMeal.idMeal} ${randomMeal.strMeal}")
                    Glide.with(this@HomeFragment)
                        .load(randomMeal.strMealThumb)
                        .into(binding.imgRandomMeal)
                }else{
                    return
                }
            }

            // Connection was unsuccessfully
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }
}