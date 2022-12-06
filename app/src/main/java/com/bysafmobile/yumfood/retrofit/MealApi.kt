package com.bysafmobile.yumfood.retrofit

import com.bysafmobile.yumfood.data.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {
    @GET("api/json/v1/1/random.php")
    fun getRandomMeal():Call<MealList>
}