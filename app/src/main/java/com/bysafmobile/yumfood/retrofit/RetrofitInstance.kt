package com.bysafmobile.yumfood.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    // Объект для запроса к серверу
    // Этот класс нужен для отправки запросов к API
    // используем класс Retrofit Builder
    val api:MealApi by lazy {
        Retrofit.Builder()
            // указываем baseUrl, содержащий необходимые данные
            .baseUrl("https://www.themealdb.com/")
            // укажем конвертер для json файла
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // передаем наш интерфейс в метод create
            .create(MealApi::class.java)
    }
}