package com.bysafmobile.yumfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bysafmobile.yumfood.pojo.Meal
import com.bysafmobile.yumfood.pojo.MealList
import com.bysafmobile.yumfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    // MutableLiveData используется для записи измененного значения
    private var randomMealLiveData = MutableLiveData<Meal>()
    // метод получения рандомного блюда
    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            // Retrofit подключен к API
            // получаем информацию о случайной еде
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                //если response не null
                if(response.body() != null){
                    // переменная randomMeal принимает из response нулевой элемент
                    val randomMeal: Meal = response.body()!!.meals[0]
                    // передаем randomMeal в значение MutableLiveData
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
    // LiveData только читает данные, а в MutableLiveData мы можем менять значение
    // LiveData используется для уведомления пользовательского интерфейса об изменении значения
    // поэтому мы вызываем этот метод из HomeFragment, чтобы прослушивать любые обновления, сделанные MutableLiveData.
    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }
}