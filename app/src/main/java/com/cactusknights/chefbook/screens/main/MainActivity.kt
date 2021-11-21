package com.cactusknights.chefbook.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityMainBinding
import com.cactusknights.chefbook.screens.main.dialogs.SettingsDialog
import com.cactusknights.chefbook.screens.categories.CategoriesFragment
import com.cactusknights.chefbook.screens.auth.AuthActivity
import com.cactusknights.chefbook.screens.favourite.FavouriteRecipesFragment
import com.cactusknights.chefbook.screens.recipes.DashboardFragment
import com.cactusknights.chefbook.screens.favourite.RecipesInCategoryFragment
import com.cactusknights.chefbook.screens.shoppinglist.ShoppingListFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val RECIPES_TAB_ID = 1
    }

    val viewModel: MainActivityViewModel by viewModels()
    private val fragmentManager: FragmentManager = supportFragmentManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState.get("state") as MainActivityState)
        }

        val navigation = NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.shopping_list) { viewModel.openFragment(DashboardFragments.SHOPPING_LIST) }
            else if (item.itemId == R.id.recipes) { viewModel.openFragment(DashboardFragments.RECIPES) }
            true
        }

        binding.nvNavigation.setOnItemSelectedListener(navigation)
        binding.btnLeft.setOnClickListener { onBackPressed() }
        binding.btnRight.setOnClickListener { onRightPressed() }

        // SQLite Migration
        val legacyDatabase = File(filesDir, "../databases/ChefBookDB")
//        if (File(filesDir, "../databases/ChefBookDB").exists())
//            FirebaseAuthRepository.migrateToFirebase(legacyDatabase, this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { currentState ->
                    if (currentState.user == null) startLoginActivity()
                    renderFragment(currentState)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("state", viewModel.state.value)
        super.onSaveInstanceState(outState)
    }

    private fun renderFragment(state: MainActivityState) {
        if (state.currentFragment != state.previousFragment) {
            var fragment = Fragment(); var startAnimation = R.anim.zoom_in_show; var endAnimation = R.anim.zoom_in_hide
            when (state.currentFragment) {
                DashboardFragments.RECIPES -> {
                    fragment = DashboardFragment()
                    if (state.previousFragment == DashboardFragments.SHOPPING_LIST) {
                        startAnimation = R.anim.swipe_left_show; endAnimation = R.anim.swipe_left_hide
                    } else if (state.previousFragment != null) {
                        startAnimation = R.anim.zoom_out_show; endAnimation = R.anim.zoom_out_hide
                    }
                }
                DashboardFragments.SHOPPING_LIST -> {
                    fragment = ShoppingListFragment(); startAnimation = R.anim.swipe_right_show; endAnimation = R.anim.swipe_right_hide }
                DashboardFragments.FAVOURITE -> { fragment = FavouriteRecipesFragment() }
                DashboardFragments.CATEGORIES -> { fragment = CategoriesFragment()
                    if (state.previousFragment == DashboardFragments.RECIPES_IN_CATEGORY) { startAnimation = R.anim.zoom_out_show; endAnimation = R.anim.zoom_out_hide }
                }
                DashboardFragments.RECIPES_IN_CATEGORY -> { fragment = RecipesInCategoryFragment() }
            }

            fragmentManager.beginTransaction()
                .setCustomAnimations(startAnimation, endAnimation)
                .replace(R.id.cv_content, fragment, state.currentFragment.name.lowercase())
                .commit()

            if (state.previousFragment == null && state.currentFragment == DashboardFragments.RECIPES) binding.nvNavigation.menu[RECIPES_TAB_ID].isChecked = true
            binding.textSection.text = resources.getString(state.currentFragment.titleId())
            if (state.currentFragment == DashboardFragments.RECIPES_IN_CATEGORY) binding.textSection.text = state.currentCategory?.name
            binding.btnLeft.visibility = if (state.currentFragment in
                arrayListOf(DashboardFragments.SHOPPING_LIST, DashboardFragments.RECIPES)) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun onRightPressed() { SettingsDialog().show(fragmentManager, "Settings") }

    override fun onBackPressed() {
        if (viewModel.state.value.currentFragment == DashboardFragments.FAVOURITE
            || viewModel.state.value.currentFragment == DashboardFragments.CATEGORIES
        ) {
            viewModel.openFragment(DashboardFragments.RECIPES)
        } else if (viewModel.state.value.currentFragment == DashboardFragments.RECIPES_IN_CATEGORY) {
            viewModel.openFragment(DashboardFragments.CATEGORIES)
        } else super.onBackPressed()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, AuthActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}