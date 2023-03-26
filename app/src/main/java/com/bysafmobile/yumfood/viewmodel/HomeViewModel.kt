package com.bysafmobile.yumfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bysafmobile.yumfood.db.MealDatabase
import com.bysafmobile.yumfood.pojo.*
import com.bysafmobile.yumfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
): ViewModel() {

    // MutableLiveData используется для записи измененного значения
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favouritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val searchedMealsLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandomMeal : Meal ? = null

    // метод получения рандомного блюда
    fun getRandomMeal(){
        saveStateRandomMeal?.let {randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }
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
                    saveStateRandomMeal = randomMeal
                }else{
                    return
                }
            }
            // если соединение неуспешно
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }
        })
    }

    // метод для получения популярных блюд в RecyclerView
    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList>{
            // Retrofit подключен к API
            // получаем список о еде
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                // если ответ не пустой
                if(response.body() != null){
                    // заполняем полученный ответ в значение MutableLiveData
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }
            // если соединение неуспешно
            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment",t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body() != null){
                    categoriesLiveData.value = response.body()!!.categories
                }
            }
            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }
        })
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun searchMeals(searchQuery: String){
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(
            object : Callback<MealList>{
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    val mealsList = response.body()?.meals
                    mealsList?.let {
                        searchedMealsLiveData.postValue(it)
                    }
                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.e("HomeViewModel", t.message.toString())
                }
            }
        )
    }

    // LiveData только читает данные, а в MutableLiveData мы можем менять значение
    // LiveData используется для уведомления пользовательского интерфейса об изменении значения
    // поэтому мы вызываем этот метод из HomeFragment, чтобы прослушивать любые обновления, сделанные MutableLiveData.
    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }

    // фун-ия для прослушивания обновлений в LiveData
    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }
    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }
    fun observeFavouritesMealsLiveData():LiveData<List<Meal>>{
        return favouritesMealsLiveData
    }
    fun observeBottomSheetMealLiveData():LiveData<Meal>{
        return bottomSheetMealLiveData
    }
    fun observeSearchedMealsLiveData():LiveData<List<Meal>>{
        return searchedMealsLiveData
    }
}