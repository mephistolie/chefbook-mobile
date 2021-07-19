package com.cactusknights.chefbook.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager2.widget.ViewPager2
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.ViewPagerAdapter
import com.cactusknights.chefbook.dialogs.ConfirmDialog
import com.cactusknights.chefbook.helpers.Utils
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates


class RecipeActivity: AppCompatActivity() {

    private var recipe = Recipe()
    private var isFavouriteOriginally by Delegates.notNull<Boolean>()
    private var allCategories = ArrayList<String>()
    private var isPremium = true

    private lateinit var toolbar: ConstraintLayout
    private lateinit var recipeName: TextView
    private lateinit var btnBack: AppCompatImageButton
    private lateinit var btnMore: AppCompatImageButton

    private lateinit var recipeViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var mAdView: AdView

    private var editRecipeRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedRecipe: Recipe? = result.data?.extras?.get("recipe") as Recipe?
            if (editedRecipe != null) {
                recipe = editedRecipe
                recipeName.text = recipe.name
                recipeViewPager.adapter = ViewPagerAdapter(recipe, supportFragmentManager, this.lifecycle)
            } else { Toast.makeText(this, resources.getString(R.string.failed_edit_recipe), Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        toolbar = findViewById(R.id.constraintLayout)
        recipeName = findViewById(R.id.recipe_name)
        btnBack = findViewById(R.id.back_button)
        btnMore = findViewById(R.id.more)
        recipeViewPager = findViewById(R.id.recipe_view_pager)
        tabLayout = findViewById(R.id.tab_dots)
        mAdView = findViewById(R.id.adView)

        recipe = if (savedInstanceState != null) {
            if (savedInstanceState.get("allCategories") != null)
                allCategories.addAll(savedInstanceState.getStringArrayList("allCategories") as ArrayList<String>)
            isPremium = savedInstanceState.get("isPremium") as Boolean
            savedInstanceState.get("recipe") as Recipe
        } else {
            if (intent.extras?.get("allCategories") != null)
                allCategories.addAll(intent.extras?.getStringArrayList("allCategories") as ArrayList<String>)
            isPremium = intent.extras?.get("isPremium") as Boolean
            intent.extras?.get("recipe") as Recipe
        }

        recipeName.text = recipe.name
        isFavouriteOriginally = recipe.isFavourite

        recipeViewPager.adapter = ViewPagerAdapter(recipe, supportFragmentManager, this.lifecycle)
        TabLayoutMediator(tabLayout, recipeViewPager) { _, _ ->}.attach()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnMore.setOnClickListener {
            openRecipeDialog()
        }

        if (!isPremium) {
            MobileAds.initialize(this)
            val adRequest: AdRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
            mAdView.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("recipe", recipe)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        if (isFavouriteOriginally != recipe.isFavourite) {
            UserViewModel.setRecipeFavoriteStatus(recipe)
            isFavouriteOriginally = recipe.isFavourite
        }
        super.onStop()
    }

    private fun openRecipeDialog() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_recipe)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val isFavourite = dialog.findViewById<ImageButton>(R.id.favourite)
        val share = dialog.findViewById<ImageButton>(R.id.share)
        val editRecipe = dialog.findViewById<ImageButton>(R.id.edit_recipe)
        val deleteRecipe = dialog.findViewById<ImageButton>(R.id.delete_recipe)
        if (recipe.isFavourite) isFavourite.setImageResource(R.drawable.ic_favorite)
        isFavourite.setOnClickListener {
            recipe.isFavourite = !recipe.isFavourite
            if (recipe.isFavourite) isFavourite.setImageResource(R.drawable.ic_favorite) else isFavourite.setImageResource(R.drawable.ic_unfavorite)
        }
        share.setOnClickListener {
            Utils.shareRecipe(recipe, resources, ::startActivity)
        }
        editRecipe.setOnClickListener {
            val intent = Intent(this, RecipeCommitActivity::class.java)
            intent.putExtra("targetRecipe", recipe)
            intent.putExtra("allCategories", allCategories)
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
            dialog.dismiss()
            editRecipeRequest.launch(intent, options)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        deleteRecipe.setOnClickListener {
            ConfirmDialog { UserViewModel.deleteRecipe(recipe, ::onDeleteRecipeCallback); finish() }.show(supportFragmentManager, "Confirm")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun onDeleteRecipeCallback(isDeleted: Boolean) {
        if (isDeleted) {
            Toast.makeText(applicationContext, resources.getString(R.string.recipe_deleted), Toast.LENGTH_SHORT).show()
            finish()
        } else { Toast.makeText(applicationContext, resources.getString(R.string.deleting_failed), Toast.LENGTH_SHORT).show()}
    }
}