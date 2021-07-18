package com.cactusknights.chefbook.activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.fragments.*
import com.cactusknights.chefbook.helpers.Dialogs
import com.cactusknights.chefbook.helpers.Dialogs.getConfirmDialog
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
            openShoppingDialog()
            return
        }
        onBackPressed()
    }

    private fun onRightPressed() { openSettingsDialog() }

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

    private fun logout() { userViewModel.logout() }

    private fun openSettingsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_settings)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userName = dialog.findViewById<TextView>(R.id.user)
        val premiumBadge = dialog.findViewById<ImageButton>(R.id.premium)
        val logoutButton = dialog.findViewById<Button>(R.id.logout)
        val aboutButton = dialog.findViewById<LinearLayoutCompat>(R.id.about_app)
        val supportButton = dialog.findViewById<LinearLayoutCompat>(R.id.buy_premium)

        val displayName = userViewModel.getCurrentUser().value?.displayName.toString()
        userName.text = if (displayName.isNotEmpty()) displayName else userViewModel.getCurrentUser().value?.email.toString()

        val shoppingListDefault = dialog.findViewById<SwitchCompat>(R.id.shopping_list_default)
        shoppingListDefault.isChecked = sp.getBoolean("shoppingListIsDefault", false)
        shoppingListDefault.setOnClickListener {
            sp.edit().putBoolean("shoppingListIsDefault", !sp.getBoolean("shoppingListIsDefault", false)).apply()
        }

        val currentUser = userViewModel.getCurrentUser().value
        if (currentUser != null && currentUser.isPremium) {
            premiumBadge.visibility = View.VISIBLE
        }

        aboutButton.setOnClickListener {
            Dialogs.openAboutDialog(this, ::sendEmail)
            dialog.dismiss()
        }
        supportButton.setOnClickListener {
            openDonateDialog()
            dialog.dismiss()
        }
        logoutButton.setOnClickListener {
            getConfirmDialog(this) { logout() }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openShoppingDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_shopping_list)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val addItem = dialog.findViewById<AppCompatButton>(R.id.add_item)
        val item = dialog.findViewById<TextView>(R.id.item)
        addItem.setOnClickListener {
            val itemText = item.text.toString()
            if (itemText.isNotEmpty()) {
                var shoppingListFragment = fragmentManager.findFragmentByTag("Shopping List")
                if (shoppingListFragment != null) {
                    shoppingListFragment = shoppingListFragment as ShoppingListFragment
                    shoppingListFragment.shoppingList.add(itemText)
                    shoppingListFragment.shoppingAdapter.notifyItemInserted(shoppingListFragment.shoppingList.size-1)
                    shoppingListFragment.emptyListTitle.visibility = if (shoppingListFragment.shoppingList.size > 0) View.GONE else View.VISIBLE
                }
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    fun openDonateDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_donate)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val smallDonation = dialog.findViewById<TextView>(R.id.small_donation)
        val middleDonation = dialog.findViewById<TextView>(R.id.middle_donation)
        val bigDonation = dialog.findViewById<TextView>(R.id.big_donation)
        val support = dialog.findViewById<TextView>(R.id.support)

        smallDonation.setOnClickListener { userViewModel.buyPremium("small_donation", this) }
        middleDonation.setOnClickListener { userViewModel.buyPremium("middle_donation", this) }
        bigDonation.setOnClickListener { userViewModel.buyPremium("big_donation", this) }
        support.setOnClickListener { sendEmail() }

        dialog.show()
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:"+resources.getString(R.string.support_email))
        startActivity(intent)
    }
}