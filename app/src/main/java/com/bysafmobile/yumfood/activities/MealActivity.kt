package com.bysafmobile.yumfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bysafmobile.yumfood.R
import com.bysafmobile.yumfood.pojo.Meal
import com.bysafmobile.yumfood.databinding.ActivityMealBinding
import com.bysafmobile.yumfood.db.MealDatabase
import com.bysafmobile.yumfood.fragments.HomeFragment
import com.bysafmobile.yumfood.viewmodel.MealViewModel
import com.bysafmobile.yumfood.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    // создадим переменные, которые используются в активити
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm:MealViewModel

    private lateinit var binding: ActivityMealBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()
        loadingCase()

        mealMvvm.getMealDetails(mealId)
        observerMealDetailsLiveData()
        onYoutubeImageClick()
        onFavouriteClick()
    }
    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_LONG).show()
            }
        }
    }

    // при нажатии на изображение Youtube
    private fun onYoutubeImageClick() {
        // откроется новый Intent
        // передадим ссылку на видео приготовление
        // конкретного блюда
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    // метод прослушивает обновления о подробной информации
    // о каждом блюде из MealViewModel
    private var mealToSave:Meal? = null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this) { t ->
            onResponseCase()
            // получаем объект t конкретное блюдо
            val meal = t
            mealToSave = meal
            // заполняем каждый элемент View через binding
            binding.tvCategory.text = "Category: ${meal!!.strCategory}"
            binding.tvArea.text = "Area : ${meal.strArea}"
            binding.tvInstructionsValue.text = meal.strInstructions
            youtubeLink = meal.strYoutube
        }
    }

    // отображаем данные: картинку в ImageView и название блюда в AppBarLayout
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)
        // отображаем название блюда в Toolbar
        binding.collapsingToolbar.title = mealName
        // поменяем цвет названия блюда
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    // получаем информацию из HomeFragment
    private fun getMealInformationFromIntent() {
        val intent = intent
        // в переменные запишем данные из intent по ключу
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    // в этом методе скрываем элементы View,
    // отображается только linearProgressbar
    private fun loadingCase(){
        binding.linearProgressbar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    // здесь прячем linearProgressbar
    // остальные элементы видимы
    private fun onResponseCase(){
        binding.linearProgressbar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}