package com.bysafmobile.yumfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bysafmobile.yumfood.R
import com.bysafmobile.yumfood.db.MealDatabase
import com.bysafmobile.yumfood.viewmodel.HomeViewModel
import com.bysafmobile.yumfood.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        // получаем navController в Activity:
        val navController = Navigation.findNavController(this, R.id.hots_fragment)
        // задаем bottomNavigation для использования с NavController
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}