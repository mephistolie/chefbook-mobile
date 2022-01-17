package com.cactusknights.chefbook.screens.recipeinput.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeCookingFragment
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeIngredientsFragment
import com.cactusknights.chefbook.models.DecryptedRecipe
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeInfoFragment
import com.cactusknights.chefbook.screens.recipeinput.fragments.RecipeInputDetailsFragment
import com.cactusknights.chefbook.screens.recipeinput.fragments.RecipeInputInfoFragment

class RecipeInputViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    private val recipeInputInfoFragment = RecipeInputInfoFragment()
    private val recipeInputDetailsFragment = RecipeInputDetailsFragment()

    override fun getItemCount(): Int { return 2 }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> recipeInputDetailsFragment
            else -> recipeInputInfoFragment
        }
    }
}