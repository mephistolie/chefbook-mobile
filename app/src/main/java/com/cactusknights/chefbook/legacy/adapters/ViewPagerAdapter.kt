package com.cactusknights.chefbook.legacy.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cactusknights.chefbook.legacy.fragments.RecipeCookingFragment
import com.cactusknights.chefbook.legacy.fragments.RecipeIngredientsFragment
import com.cactusknights.chefbook.models.Recipe

class ViewPagerAdapter(val recipe: Recipe, fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int { return 2 }

    override fun createFragment(position: Int): Fragment {
        return if(position == 1) RecipeCookingFragment(recipe)
            else RecipeIngredientsFragment(recipe)
    }
}