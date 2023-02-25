package com.bysafmobile.yumfood.pojo


import com.google.gson.annotations.SerializedName

data class CategoryMeals(
    @SerializedName("idMeal")
    val idMeal: String, // 52959
    @SerializedName("strMeal")
    val strMeal: String, // Baked salmon with fennel & tomatoes
    @SerializedName("strMealThumb")
    val strMealThumb: String // https://www.themealdb.com/images/media/meals/1548772327.jpg
)