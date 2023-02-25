package com.bysafmobile.yumfood.pojo


import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("meals")
    val meals: List<CategoryMeals>
)