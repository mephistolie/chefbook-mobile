package com.cactusknights.chefbook.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.ViewPagerAdapter
import com.cactusknights.chefbook.databinding.ActivityRecipeBinding
import com.cactusknights.chefbook.dialogs.RecipeDialog
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.viewmodels.UserViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates


class RecipeActivity: AppCompatActivity() {

    var recipe = Recipe()
    private var isFavouriteOriginally by Delegates.notNull<Boolean>()
    var allCategories = ArrayList<String>()
    private var isPremium = true

    var editRecipeRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedRecipe: Recipe? = result.data?.extras?.get("recipe") as Recipe?
            if (editedRecipe != null) {
                recipe = editedRecipe
                binding.textSection.text = recipe.name
                binding.vpRecipe.adapter = ViewPagerAdapter(recipe, supportFragmentManager, this.lifecycle)
            } else { Toast.makeText(this, resources.getString(R.string.failed_edit_recipe), Toast.LENGTH_SHORT).show() }
        }
    }

    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipe = if (savedInstanceState != null) {
            if (savedInstanceState.get("allCategories") != null)
                allCategories.addAll(savedInstanceState.getStringArrayList("allCategories") as ArrayList<String>)
            val premium = savedInstanceState.get("isPremium") as Boolean?
            if (premium != null) isPremium = premium
            savedInstanceState.get("recipe") as Recipe
        } else {
            if (intent.extras?.get("allCategories") != null)
                allCategories.addAll(intent.extras?.getStringArrayList("allCategories") as ArrayList<String>)
            val premium = intent.extras?.get("isPremium") as Boolean?
            if (premium != null) isPremium = premium
            intent.extras?.get("recipe") as Recipe
        }

        binding.textSection.text = recipe.name
        isFavouriteOriginally = recipe.isFavourite

        binding.vpRecipe.adapter = ViewPagerAdapter(recipe, supportFragmentManager, this.lifecycle)
        TabLayoutMediator(binding.tabDots, binding.vpRecipe) { _, _ ->}.attach()

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnMore.setOnClickListener { RecipeDialog(this).show(supportFragmentManager, "Recipe Menu") }

        if (!isPremium) {
            MobileAds.initialize(this)
            val adRequest: AdRequest = AdRequest.Builder().build()
            binding.avBanner.loadAd(adRequest)
            binding.avBanner.visibility = View.VISIBLE
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
}