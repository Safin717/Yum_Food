package com.bysafmobile.yumfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
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