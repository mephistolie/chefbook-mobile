package com.cactusknights.chefbook.screens.recipe.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeCookingFragment
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeIngredientsFragment
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeInfoFragment

class RecipeViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    private val recipeInfoFragment = RecipeInfoFragment()
    private val recipeCookingFragment = RecipeCookingFragment()
    private val recipeIngredientsFragment = RecipeIngredientsFragment()

    override fun getItemCount(): Int { return 3 }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> recipeIngredientsFragment
            2 -> recipeCookingFragment
            else -> recipeInfoFragment
        }
    }
}