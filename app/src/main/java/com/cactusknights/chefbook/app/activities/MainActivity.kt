package com.cactusknights.chefbook.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.app.fragments.RecipesFragment
import com.cactusknights.chefbook.app.helpers.Utils.viewModelFactory
import com.cactusknights.chefbook.app.viewmodels.UserViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private val recipesFragment = RecipesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = ViewModelProvider(this, viewModelFactory{UserViewModel(this.application)}).get(UserViewModel::class.java)
        if (!userViewModel.isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        setFragment(recipesFragment)
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .commit()
    }
}