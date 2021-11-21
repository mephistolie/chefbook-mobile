package com.cactusknights.chefbook.screens.recipe.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeCookingFragment
import com.cactusknights.chefbook.screens.recipe.fragments.RecipeIngredientsFragment
import com.cactusknights.chefbook.models.DecryptedRecipe

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int { return 2 }

    override fun createFragment(position: Int): Fragment {
        return if(position == 1) RecipeCookingFragment()
            else RecipeIngredientsFragment()
    }
}