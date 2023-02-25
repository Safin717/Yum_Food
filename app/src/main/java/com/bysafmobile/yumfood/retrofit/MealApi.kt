package com.bysafmobile.yumfood.retrofit

import com.bysafmobile.yumfood.pojo.CategoryList
import com.bysafmobile.yumfood.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    // определим конечные точки внутри интерфейса
    @GET("api/json/v1/1/random.php")
    // посылаем get запрос на сервер
    fun getRandomMeal():Call<MealList>

    // get запрос с параметром id, по которому
    // получаем данные о конкретном Meal
    @GET("api/json/v1/1/lookup.php?")
    fun getMealDetails(@Query("i") id: String):Call<MealList>

    // get запрос для горизонтального RecyclerView
    @GET("api/json/v1/1/filter.php?")
    fun getPopularItems(@Query("c") categoryName:String): Call<CategoryList>

}