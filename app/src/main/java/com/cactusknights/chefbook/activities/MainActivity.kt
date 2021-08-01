package com.cactusknights.chefbook.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityMainBinding
import com.cactusknights.chefbook.dialogs.SettingsDialog
import com.cactusknights.chefbook.dialogs.ShoppingListDialog
import com.cactusknights.chefbook.fragments.CategoriesFragment
import com.cactusknights.chefbook.fragments.DashboardFragment
import com.cactusknights.chefbook.fragments.ShoppingListFragment
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.FirebaseAuthRepository
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var userViewModel: UserViewModel
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val innerFragments = arrayListOf("Favourite", "Categories", "Recipes in Category")
    private lateinit var sp: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("ChefBook", MODE_PRIVATE)

        if (savedInstanceState != null) {
            val savedFragment = supportFragmentManager.getFragment(savedInstanceState, "savedFragment")
            if (savedFragment != null) {
                val savedSectionName = savedInstanceState.getString("sectionName", resources.getString(R.string.recipes))
                val tag = savedFragment.tag!!
                when (savedFragment.tag) {
                    "Favourite" -> { setTopMenu(resources.getString(R.string.favourite), true) }
                    "Categories" -> { setTopMenu(resources.getString(R.string.categories), true) }
                    "Recipes in Category" -> { setTopMenu(savedSectionName, true) }
                }
                setFragment(savedFragment, R.anim.zoom_in_show, R.anim.zoom_in_hide, tag)
            } else { setStartFragment() }
        } else { setStartFragment() }

        val navigation = NavigationBarView.OnItemSelectedListener { item ->
            val shoppingListFragment = fragmentManager.findFragmentByTag("Shopping List")
            val recipesFragment = fragmentManager.findFragmentByTag("Recipes")
            if (item.itemId == R.id.shopping_list && (shoppingListFragment == null || !shoppingListFragment.isVisible)) {
                setTopMenu(resources.getString(R.string.shopping_list), true, R.drawable.ic_add, R.color.deep_orange_light)
                setFragment(ShoppingListFragment(), R.anim.swipe_right_show, R.anim.swipe_right_hide, "Shopping List")
            } else if (item.itemId == R.id.recipes && (recipesFragment == null || !recipesFragment.isVisible)) {
                setTopMenu()
                if (shoppingListFragment != null && shoppingListFragment.isVisible) setFragment(DashboardFragment(), R.anim.swipe_left_show, R.anim.swipe_left_hide, "Recipes")
                else setFragment(DashboardFragment(), R.anim.zoom_out_show, R.anim.zoom_out_hide, "Recipes")
            }
            true
        }
        binding.nvNavigation.setOnItemSelectedListener(navigation)
        binding.btnLeft.setOnClickListener { onLeftPressed() }
        binding.btnRight.setOnClickListener { onRightPressed() }

        // SQLite Migration
        val legacyDatabase = File(filesDir, "../databases/ChefBookDB")
        if (File(filesDir, "../databases/ChefBookDB").exists())
            FirebaseAuthRepository.migrateToFirebase(legacyDatabase, this)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onStart() {
        lifecycleScope.launch {
            userViewModel.startListeningToUpdates()
            listenToUser()
        }
        super.onStart()
    }

    private suspend fun listenToUser() {
        userViewModel.listenToUser().collect { user: User? ->
            if (user == null) {
                startLoginActivity()
            } else {
                if (user.isPremium)
                    binding.avBanner.visibility = View.GONE
                else {
                    MobileAds.initialize(this)
                    val adRequest: AdRequest = AdRequest.Builder().build()
                    binding.avBanner.loadAd(adRequest)
                    binding.avBanner.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        userViewModel.stopListeningToUpdates()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) {
                fragmentManager.putFragment(outState, "savedFragment", fragment)
                outState.putString("sectionName", binding.textSection.text.toString())
            }
        }
        super.onSaveInstanceState(outState)
    }

    private fun setStartFragment() {
        if (!sp.getBoolean("shoppingListIsDefault", false)) {
            setFragment(DashboardFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Recipes")
            binding.nvNavigation.selectedItemId = R.id.recipes
        } else {
            setTopMenu(resources.getString(R.string.shopping_list), true, R.drawable.ic_add, R.color.deep_orange_light)
            setFragment(ShoppingListFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Shopping List")
        }
    }

    fun setFragment(fragment: Fragment, startAnimation: Int, endAnimation: Int, tag: String = "") {
        fragmentManager.beginTransaction()
            .setCustomAnimations(startAnimation, endAnimation)
            .replace(R.id.cv_content, fragment, tag)
            .commit()
    }

    fun setTopMenu(title: String = resources.getString(R.string.recipes),
                           isLeftVisible: Boolean = false,
                           leftIcon: Int = R.drawable.ic_back,
                           leftColor: Int = R.color.secondary_text_tint) {

        binding.textSection.text = title
        if (isLeftVisible) {
            binding.btnLeft.setImageResource(leftIcon)
            binding.btnLeft.background =
                if (leftIcon == R.drawable.ic_back)
                    ResourcesCompat.getDrawable(resources, R.drawable.ripple_secondary, null)
                else
                    ResourcesCompat.getDrawable(resources, R.drawable.ripple_primary, null)
            binding.btnLeft.visibility = View.VISIBLE
            binding.btnLeft.setColorFilter(ContextCompat.getColor(this, leftColor))
        } else {
            binding.btnLeft.visibility = View.INVISIBLE
        }
    }

    private fun onLeftPressed() {
        val currentFragment = fragmentManager.findFragmentByTag("Shopping List")
        if (currentFragment != null && currentFragment.isVisible) {
            ShoppingListDialog().show(fragmentManager, "Shopping List Dialog")
            return
        }
        onBackPressed()
    }

    private fun onRightPressed() { SettingsDialog().show(fragmentManager, "Settings") }

    override fun onBackPressed() {
        for (fragment in innerFragments) {
            val currentFragment = fragmentManager.findFragmentByTag(fragment)
            if (currentFragment != null && currentFragment.isVisible) {
                if (fragment == "Recipes in Category") {
                    setTopMenu(resources.getString(R.string.categories), true)
                    setFragment(CategoriesFragment(), R.anim.zoom_out_show, R.anim.zoom_out_hide, "Categories")
                } else {
                    setTopMenu()
                    setFragment(DashboardFragment(), R.anim.zoom_out_show, R.anim.zoom_out_hide, "Recipes")
                }
                return
            }
        }
        super.onBackPressed()
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}