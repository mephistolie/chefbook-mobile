package com.cactusknights.chefbook.screens.recipeinput

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.databinding.ActivityRecipeInputBinding
import com.cactusknights.chefbook.common.ConfirmDialog
import com.cactusknights.chefbook.common.showToast
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.screens.recipe.adapters.RecipeViewPagerAdapter
import com.cactusknights.chefbook.screens.recipeinput.adapters.RecipeInputViewPagerAdapter
import com.cactusknights.chefbook.screens.recipeinput.models.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RecipeInputActivity: AppCompatActivity() {

    val viewModel: RecipeInputViewModel by viewModels()
    private lateinit var binding: ActivityRecipeInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.obtainEvent(RecipeInputEvent.CreateRecipe)
        if (savedInstanceState == null) {
            val originalRecipe = intent.extras?.get("recipe") as DecryptedRecipe?
            if (originalRecipe != null) viewModel.obtainEvent(RecipeInputEvent.EditRecipe(originalRecipe))
        }

        binding.vpRecipeInput.adapter = RecipeInputViewPagerAdapter(supportFragmentManager, this.lifecycle)
        binding.tabDots.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (binding.tabDots.selectedTabPosition+1 == binding.tabDots.tabCount) {
                    binding.btnConfirm.setImageDrawable(ContextCompat.getDrawable(this@RecipeInputActivity, R.drawable.ic_check))
                } else {
                    binding.btnConfirm.setImageDrawable(ContextCompat.getDrawable(this@RecipeInputActivity, R.drawable.ic_forward))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        TabLayoutMediator(binding.tabDots, binding.vpRecipeInput) { _, _ -> }.attach()

        binding.btnBack.setOnClickListener {
            when (binding.vpRecipeInput.currentItem) {
                0 -> onBackPressed()
                else -> binding.vpRecipeInput.currentItem = binding.vpRecipeInput.currentItem-1
            }

        }
        binding.btnConfirm.setOnClickListener {
            when (binding.vpRecipeInput.currentItem) {
                1 -> viewModel.obtainEvent(RecipeInputEvent.ConfirmInput)
                else -> binding.vpRecipeInput.currentItem = binding.vpRecipeInput.currentItem+1
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.recipeInputState.collect { render(it) } }
                launch { viewModel.viewEffect.collect { handleViewEffect(it) } }
            }
        }
    }

    private fun render(state: RecipeInputState) {
        if (state is RecipeInputState.EditRecipe) { binding.textSection.setText(R.string.edit_recipe) }
    }

    private fun handleViewEffect(effect: RecipeInputViewEffect) {
        when (effect) {
            is RecipeInputViewEffect.Message -> this@RecipeInputActivity.showToast(effect.messageId)
            is RecipeInputViewEffect.InputConfirmed -> {
                setResult(RESULT_OK, Intent().putExtra("recipe", effect.committedRecipe))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        ConfirmDialog { super.onBackPressed() }.show(supportFragmentManager, "Confirm")
    }
}