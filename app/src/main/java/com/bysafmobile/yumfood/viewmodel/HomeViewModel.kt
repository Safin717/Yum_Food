package com.bysafmobile.yumfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bysafmobile.yumfood.data.Meal
import com.bysafmobile.yumfood.data.MealList
import com.bysafmobile.yumfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            // Retrofit подключен к API
            // получаем информацию о случайной еде
            // и показываем его в imageView
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                //если response not null
                if(response.body() != null){
                    // переменная randomMeal принимает из response нулевой элемент
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d("Test", "meal id ${randomMeal.idMeal} ${randomMeal.strMeal}")
                    randomMealLiveData.value = randomMeal
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
    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }
}