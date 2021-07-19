package com.cactusknights.chefbook.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.dialogs.SettingsDialog
import com.cactusknights.chefbook.dialogs.ShoppingDialog
import com.cactusknights.chefbook.fragments.*
import com.cactusknights.chefbook.models.User
import com.cactusknights.chefbook.repositories.FirebaseAuthRepository
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var userViewModel: UserViewModel
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val innerFragments = arrayListOf("Favourite", "Categories", "Recipes in Category")
    private lateinit var sp: SharedPreferences

    private lateinit var leftButton: ImageButton
    private lateinit var sectionName: TextView
    private lateinit var rightButton: ImageButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leftButton = findViewById(R.id.left_button)
        sectionName = findViewById(R.id.section_name)
        rightButton = findViewById(R.id.right_button)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        mAdView = findViewById(R.id.adView)

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
        bottomNavigationView.setOnItemSelectedListener(navigation)
        leftButton.setOnClickListener { onLeftPressed() }
        rightButton.setOnClickListener { onRightPressed() }

        // SQLite Migration
        val legacyDatabase = File(filesDir, "../databases/ChefBookDB")
        if (File(filesDir, "../databases/ChefBookDB").exists())
            FirebaseAuthRepository.migrateToFirebase(legacyDatabase, this)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.listenForUpdates()
    }

    override fun onStart() {
        userViewModel.getCurrentUser().observe(this, { user: User? ->
            if (user == null) {
                startLoginActivity()
            } else {
                if (user.isPremium)
                    mAdView.visibility = View.GONE
                else {
                    MobileAds.initialize(this)
                    val adRequest: AdRequest = AdRequest.Builder().build()
                    mAdView.loadAd(adRequest)
                    mAdView.visibility = View.VISIBLE
                }
            }
        })
        super.onStart()
    }

    override fun onDestroy() {
        userViewModel.stopListening()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) {
                fragmentManager.putFragment(outState, "savedFragment", fragment)
                outState.putString("sectionName", sectionName.text.toString())
            }
        }
        super.onSaveInstanceState(outState)
    }

    private fun setStartFragment() {
        if (!sp.getBoolean("shoppingListIsDefault", false)) {
            setFragment(DashboardFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Recipes")
            bottomNavigationView.selectedItemId = R.id.recipes
        } else {
            setTopMenu(resources.getString(R.string.shopping_list), true, R.drawable.ic_add, R.color.deep_orange_light)
            setFragment(ShoppingListFragment(), R.anim.zoom_in_show, R.anim.zoom_in_hide,"Shopping List")
        }
    }

    fun setFragment(fragment: Fragment, startAnimation: Int, endAnimation: Int, tag: String = "") {
        fragmentManager.beginTransaction()
            .setCustomAnimations(startAnimation, endAnimation)
            .replace(R.id.content, fragment, tag)
            .commit()
    }

    fun setTopMenu(title: String = resources.getString(R.string.recipes),
                           isLeftVisible: Boolean = false,
                           leftIcon: Int = R.drawable.ic_back,
                           leftColor: Int = R.color.login_tint,
                           rightIcon: Int = R.drawable.ic_user) {

        sectionName.text = title
        if (isLeftVisible) {
            leftButton.setImageResource(leftIcon)
            leftButton.visibility = View.VISIBLE
            leftButton.setColorFilter(ContextCompat.getColor(this, leftColor))
        } else {
            leftButton.visibility = View.INVISIBLE
        }
        rightButton.setImageResource(rightIcon)
    }

    private fun onLeftPressed() {
        val currentFragment = fragmentManager.findFragmentByTag("Shopping List")
        if (currentFragment != null && currentFragment.isVisible) {
            ShoppingDialog().show(fragmentManager, "Shopping List Dialog")
            return
        }
        onBackPressed()
    }

    private fun onRightPressed() { SettingsDialog().show(supportFragmentManager, "Settings") }

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