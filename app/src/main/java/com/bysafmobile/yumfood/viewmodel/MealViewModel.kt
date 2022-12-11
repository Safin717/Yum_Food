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

class MealViewModel():ViewModel() {

    // MutableLiveData используется для записи измененного значения
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    // получаем информацию об определенном Meal по id
    // показываем его в imageView
    fun getMealDetails(id:String){
        // используем экзмеляр класса RetrofitInstance
        // вызываем метод getMealDetails(id)
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                // если ответ от сервера не null
                if(response.body() != null){
                    // запишем в значение mealDetailsLiveData нулевой элемент из ответа
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }
                else
                    return
            }
            // если подключение не успешно
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())

            }
        })
    }

    // LiveData используется для уведомления пользовательского интерфейса об изменении значения
    // поэтому мы вызываем этот метод из MealActivity
    fun observeMealDetailLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }
}