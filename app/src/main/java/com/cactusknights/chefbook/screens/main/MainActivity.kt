package com.cactusknights.chefbook.screens.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.databinding.ActivityMainBinding
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.SettingsScheme
import com.cactusknights.chefbook.screens.auth.AuthActivity
import com.cactusknights.chefbook.screens.main.fragments.profile.dialogs.AboutDialog
import com.cactusknights.chefbook.screens.main.fragments.recipesincategory.RecipesInCategoryFragment
import com.cactusknights.chefbook.screens.main.models.NavigationEvent
import com.cactusknights.chefbook.screens.main.models.NavigationEffect
import com.cactusknights.chefbook.screens.main.fragments.profile.dialogs.BroccoinsDialog
import com.cactusknights.chefbook.screens.main.fragments.profile.dialogs.SubscriptionDialog
import com.cactusknights.chefbook.screens.recipe.RecipeActivity
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputActivity
import com.google.android.material.navigation.NavigationBarView
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore : DataStore<Preferences>

    companion object {
        const val IS_BACK_BUTTON_VISIBLE = "IS_BACK_BUTTON_VISIBLE"
        const val TITLE = "TITLE"
        const val ABOUT_DIALOG = "About Dialog"
        const val SUBSCRIPTION_DIALOG = "Subscription"
        private var isFirstLaunch = true
    }

    private val viewModel: NavigationViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding
    private val navBuilder = NavOptions.Builder()

    private val rootRoutes = listOf(R.id.recipesFragment, R.id.shoppingListFragment, R.id.profileFragment)
    private val profileRoutes = listOf(R.id.profileFragment, R.id.settingsFragment, R.id.editProfileFragment)

    private var isShoppingListDefault = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            val isBackBtnVisible : Boolean = savedInstanceState.getBoolean(IS_BACK_BUTTON_VISIBLE)
            val title = savedInstanceState.getString(TITLE, resources.getString(R.string.shopping_list))
            binding.textSection.text = title; binding.btnBack.visibility = if (isBackBtnVisible) View.VISIBLE else View.INVISIBLE
            super.onRestoreInstanceState(savedInstanceState)
        }

        val navigation = NavigationBarView.OnItemSelectedListener { item ->
            if (item.itemId == R.id.shopping_list) { viewModel.obtainEvent(NavigationEvent.OpenShoppingListFragment) }
            else if (item.itemId == R.id.recipes) { viewModel.obtainEvent(NavigationEvent.OpenRecipesFragment) }
            else { viewModel.obtainEvent(NavigationEvent.OpenProfile) }
            true
        }

        navController = (supportFragmentManager.findFragmentById(R.id.cv_content) as NavHostFragment).navController

        binding.nvNavigation.selectedItemId = R.id.recipes
        binding.nvNavigation.setOnItemSelectedListener(navigation)
        binding.btnBack.setOnClickListener { onBackPressed() }

        // SQLite Migration
//        val legacyDatabase = File(filesDir, "../databases/ChefBookDB")
//        if (File(filesDir, "../databases/ChefBookDB").exists())
//            FirebaseAuthRepository.migrateToFirebase(legacyDatabase, this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.viewEffect.collect { handleViewEffect(it) } }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = dataStore.data.first()
            isShoppingListDefault = prefs[SettingsScheme.FIELD_SHOPPING_LIST_DEFAULT]?:false
            withContext(Dispatchers.Main) {
                val systemTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                when (prefs[SettingsScheme.FIELD_THEME]?:0) {
                    1 -> if (systemTheme == Configuration.UI_MODE_NIGHT_YES)AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    2 -> if (systemTheme == Configuration.UI_MODE_NIGHT_NO) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                if (isFirstLaunch && isShoppingListDefault) {
                    isFirstLaunch = false
                    binding.nvNavigation.selectedItemId = R.id.shopping_list
                    navController.navigate(R.id.shoppingListFragment)
                }
            }
        }
    }

    private fun handleViewEffect(state: NavigationEffect) {
        if (state is NavigationEffect.SignedOut) return startAuthActivity()
        val destinationId = navController.currentDestination?.id
        when (state) {
            is NavigationEffect.ShoppingListFragment -> {
                if (destinationId != R.id.shoppingListFragment) {
                    val navOptions = createBottomNavOptions(toTheLeft = true)
                    navController.navigate(R.id.shoppingListFragment, null, navOptions)
                }
                binding.textSection.text = resources.getString(R.string.shopping_list)
            }
            is NavigationEffect.RecipesFragment -> {
                if (destinationId == R.id.categoriesFragment || destinationId == R.id.favouriteRecipesFragment) {
                    navController.navigateUp()
                } else if (destinationId == R.id.shoppingListFragment || destinationId in profileRoutes) {
                    val navOptions = if (destinationId == R.id.shoppingListFragment) createBottomNavOptions() else createBottomNavOptions(toTheLeft = true)
                    navController.navigate(R.id.recipesNavigation, null, navOptions)
                }
                binding.textSection.text = resources.getString(R.string.recipes)
            }
            is NavigationEffect.ProfileFragment -> {
                if (destinationId == R.id.settingsFragment) navController.navigateUp()
                else if (destinationId == R.id.shoppingListFragment || destinationId == R.id.recipesFragment) {
                    val navOptions = createBottomNavOptions()
                    navController.navigate(R.id.profileNavigation, null, navOptions)
                }
                binding.textSection.text = resources.getString(R.string.profile)
            }
            is NavigationEffect.FavouriteFragment -> {
                navController.navigate(R.id.action_recipesFragment_to_favouriteRecipesFragment)
                binding.textSection.text = resources.getString(R.string.favourite)
            }
            is NavigationEffect.CategoriesFragment -> {
                if (navController.currentDestination?.id == R.id.recipesInCategoryFragment) {
                    navController.navigateUp()
                } else {
                    navController.navigate(R.id.action_recipesFragment_to_categoriesFragment)
                }
                binding.textSection.text = resources.getString(R.string.categories)
            }
            is NavigationEffect.CategoryScreen -> {
                navController.navigate(R.id.action_categoriesFragment_to_recipesInCategoryFragment, bundleOf(
                    RecipesInCategoryFragment.CATEGORY_ID to state.category.id!!
                ))
                binding.textSection.text = state.category.name
            }
            is NavigationEffect.EditProfileDialog -> {
                navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                binding.textSection.text = resources.getString(R.string.edit_profile)
            }
            is NavigationEffect.SettingsFragment -> {
                val sv = findViewById<ScrollView>(R.id.sv_menu)
                navController.navigate(R.id.action_profileFragment_to_settingsFragment)
                binding.textSection.text = resources.getString(R.string.settings)
            }
            is NavigationEffect.RateAppScreen -> { requestReviewFlow() }
            is NavigationEffect.AboutAppDialog -> { AboutDialog().show(supportFragmentManager, ABOUT_DIALOG) }
            is NavigationEffect.AddRecipe -> { addRecipe() }
            is NavigationEffect.RecipeOpened -> openRecipe(state.recipe)
            is NavigationEffect.SignedOut -> startAuthActivity()
            is NavigationEffect.SubscriptionDialog -> SubscriptionDialog().show(supportFragmentManager, SUBSCRIPTION_DIALOG)
            NavigationEffect.BroccoinsDialog -> BroccoinsDialog().show(supportFragmentManager, ABOUT_DIALOG)
        }
        binding.btnBack.visibility = if (navController.currentDestination?.id in rootRoutes) View.GONE else View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_BACK_BUTTON_VISIBLE, binding.btnBack.visibility == View.VISIBLE)
        outState.putString(TITLE, binding.textSection.text.toString())
    }

    private fun addRecipe() {
        val intent = Intent(this, RecipeInputActivity()::class.java)
        startActivity(intent)
    }

    private fun openRecipe(recipe: Recipe) {
        val intent = Intent(this, RecipeActivity()::class.java)
        intent.putExtra("recipe", recipe)
        val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() {
        val destinationId = navController.currentDestination?.id
        if (destinationId == R.id.categoriesFragment || destinationId == R.id.favouriteRecipesFragment) viewModel.obtainEvent(NavigationEvent.OpenRecipesFragment)
        else if (destinationId == R.id.settingsFragment) viewModel.obtainEvent(NavigationEvent.OpenProfile)
        else if (destinationId == R.id.recipesInCategoryFragment) viewModel.obtainEvent(NavigationEvent.OpenCategoriesFragment)
        else super.onBackPressed()
    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
        this.viewModelStore.clear()
    }

    private fun createBottomNavOptions(toTheLeft: Boolean = false) : NavOptions {
        return navBuilder
            .setPopUpTo(R.id.nav_graph_application, true)
            .setEnterAnim(if (toTheLeft) R.anim.swipe_right_show else R.anim.swipe_left_show)
            .setExitAnim(if (toTheLeft) R.anim.swipe_right_hide else R.anim.swipe_left_hide)
            .build()
    }

    private fun requestReviewFlow() {
        val manager = ReviewManagerFactory.create(this)
        val requestReviewFlow = manager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        this.showToast(R.string.rate_gratitude)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}