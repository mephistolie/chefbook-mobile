package com.cactusknights.chefbook.screens.recipe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityRecipeBinding
import com.cactusknights.chefbook.screens.recipe.dialogs.RecipeDialog
import com.cactusknights.chefbook.legacy.helpers.showToast
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.screens.recipe.adapters.ViewPagerAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeActivity: AppCompatActivity() {

    val viewModel: RecipeViewModel by viewModels()

    var editRecipeRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedRecipe: DecryptedRecipe? = result.data?.extras?.get("recipe") as DecryptedRecipe?
            if (editedRecipe != null) {
                viewModel.setRecipe(editedRecipe)
            } else { applicationContext.showToast(resources.getString(R.string.failed_edit_recipe)) }
        }
    }

    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) { viewModel.restoreState(savedInstanceState.get("state") as RecipeActivityState) }
        else { viewModel.setRecipe(intent.extras?.get("recipe") as DecryptedRecipe) }

        binding.vpRecipe.adapter = ViewPagerAdapter(supportFragmentManager, this.lifecycle)
        TabLayoutMediator(binding.tabDots, binding.vpRecipe) { _, _ -> }.attach()

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnMore.setOnClickListener { RecipeDialog(this).show(supportFragmentManager, "Recipe Menu") }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.message != null) this@RecipeActivity.showToast(state.message)

                    if (state.isDeleted) finish()
                    binding.textSection.text = state.recipe.name
                    if (!state.isPremium) {
                        MobileAds.initialize(this@RecipeActivity)
                        val adRequest: AdRequest = AdRequest.Builder().build()
                        binding.avBanner.loadAd(adRequest)
                        binding.avBanner.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("state", viewModel.state.value)
        super.onSaveInstanceState(outState)
    }
}