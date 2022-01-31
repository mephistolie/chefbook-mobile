package com.cactusknights.chefbook.screens.recipe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.util.CoilUtils
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.core.retrofit.interceptors.EncryptedDataInterceptor
import com.cactusknights.chefbook.databinding.ActivityRecipeBinding
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.screens.recipe.adapters.RecipeViewPagerAdapter
import com.cactusknights.chefbook.screens.recipe.dialogs.RecipeDialog
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.screens.recipe.models.RecipeScreenViewEffect
import com.cactusknights.chefbook.screens.recipeinput.RecipeInputActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity() {

    val viewModel: RecipeViewModel by viewModels()

    private val recipeMenu = RecipeDialog()

    private lateinit var picturesLoader : ImageLoader

    var editRecipeRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val editedRecipe: DecryptedRecipe? =
                result.data?.extras?.get("recipe") as DecryptedRecipe?
            if (editedRecipe != null) {
                viewModel.obtainEvent(RecipeScreenEvent.LoadRecipe(editedRecipe, this))
            } else {
                applicationContext.showToast(resources.getString(R.string.failed_edit_recipe))
            }
        }
    }

    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        picturesLoader = imageLoader

        val uri = intent.data
        if (uri != null) {
            val url = uri.toString().substring(uri.toString().indexOfLast { it == '/' } + 1)
            viewModel.obtainEvent(RecipeScreenEvent.LoadRecipeByRemoteId(url.toInt()))
        } else {
            val recipe = intent.extras?.get("recipe") as DecryptedRecipe
            viewModel.obtainEvent(RecipeScreenEvent.LoadRecipe(recipe, this))
        }

        binding.svRecipe.adapter = RecipeViewPagerAdapter(supportFragmentManager, this.lifecycle)
        TabLayoutMediator(binding.tlRecipe, binding.svRecipe) { tab, position ->
            tab.text = when (position) {
                1 -> resources.getString(R.string.ingredients)
                2 -> resources.getString(R.string.cooking)
                else -> resources.getString(R.string.info)
            }
        }.attach()

        TabLayoutMediator(binding.tabDots, binding.svRecipe) { _, _ -> }.attach()

        binding.tlRecipe.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.textTab.text = when (binding.tlRecipe.selectedTabPosition) {
                    1 -> resources.getString(R.string.ingredients)
                    2 -> resources.getString(R.string.cooking)
                    else -> resources.getString(R.string.info)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                binding.root.transitionToEnd()
            }
        })

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnMore.setOnClickListener {
            recipeMenu.show(supportFragmentManager, "Recipe Menu")
        }

        binding.btnLiked.setOnClickListener { viewModel.obtainEvent(RecipeScreenEvent.ChangeLikeStatus) }
        binding.btnFavourite.setOnClickListener { viewModel.obtainEvent(RecipeScreenEvent.ChangeFavouriteStatus) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.viewEffect.collect { handleViewEffect(it) } }
                launch { viewModel.recipeState.collect { render(it) } }
            }
        }
    }

    private fun render(state: RecipeScreenState) {
        when (state) {
            RecipeScreenState.Loading -> {}
            is RecipeScreenState.DataUpdated -> {
                if (state.key != null && picturesLoader == imageLoader) {
                    picturesLoader = ImageLoader.Builder(this)
                        .okHttpClient {
                            OkHttpClient.Builder()
                                .cache(CoilUtils.createDefaultCache(this))
                                .addInterceptor(EncryptedDataInterceptor(this, state.key))
                                .build()
                        }.build()
                }

                binding.textSection.text = state.recipe.name

                if (!state.recipe.preview.isNullOrEmpty()) {
                    binding.cvCover.visibility = View.VISIBLE
                    val request = ImageRequest.Builder(this)
                        .data(state.recipe.preview)
                        .target(binding.ivCover)
                        .crossfade(true)
                        .build()
                    picturesLoader.enqueue(request)
                } else binding.cvCover.visibility = View.GONE

                binding.textName.text = state.recipe.name
                binding.textAuthorPlaceholder.text = state.recipe.ownerName
                binding.textLikes.text = state.recipe.likes.toString()
                if (state.recipe.isLiked) {
                    binding.btnLiked.setImageDrawable(ContextCompat.getDrawable(this@RecipeActivity, R.drawable.ic_like))
                    binding.btnLiked.setColorFilter(ContextCompat.getColor(this@RecipeActivity, R.color.orange_light))
                } else {
                    binding.btnLiked.setImageDrawable(ContextCompat.getDrawable(this@RecipeActivity, R.drawable.ic_unliked))
                    binding.btnLiked.setColorFilter(ContextCompat.getColor(this@RecipeActivity, R.color.navigation_ripple))
                }
                if (state.recipe.isFavourite) {
                    binding.btnFavourite.setImageDrawable(ContextCompat.getDrawable(this@RecipeActivity, R.drawable.ic_favorite))
                    binding.btnFavourite.setColorFilter(ContextCompat.getColor(this@RecipeActivity, R.color.red_light))
                } else {
                    binding.btnFavourite.setImageDrawable(ContextCompat.getDrawable(this@RecipeActivity, R.drawable.ic_unfavourite))
                    binding.btnFavourite.setColorFilter(ContextCompat.getColor(this@RecipeActivity, R.color.navigation_ripple))
                }

                binding.btnAddToRecipes.visibility =
                    if (state.recipe.isOwned) View.GONE else View.VISIBLE
                binding.btnMore.visibility = if (state.recipe.isOwned) View.VISIBLE else View.GONE
            }
        }
    }

    private fun handleViewEffect(effect: RecipeScreenViewEffect) {
        when (effect) {
            is RecipeScreenViewEffect.Message -> this.showToast(effect.messageId)
            is RecipeScreenViewEffect.EnableAds -> {
                MobileAds.initialize(this@RecipeActivity)
                val adRequest: AdRequest = AdRequest.Builder().build()
                binding.avBanner.loadAd(adRequest)
                binding.avBanner.visibility = View.VISIBLE
            }
            is RecipeScreenViewEffect.EditRecipe -> {
                val intent = Intent(this, RecipeInputActivity::class.java)
                intent.putExtra("recipe", effect.recipe)
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this)
                editRecipeRequest.launch(intent, options)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            is RecipeScreenViewEffect.RecipeDeleted -> finish()
        }
    }
}