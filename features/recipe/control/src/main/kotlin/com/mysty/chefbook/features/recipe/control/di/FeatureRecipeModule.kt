package com.mysty.chefbook.features.recipe.control.di

import com.mysty.chefbook.features.recipe.control.ui.RecipeControlScreenViewModel
import com.mysty.chefbook.features.recipe.control.ui.components.categories.RecipeCategoriesSelectionBlockViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureRecipeControlModule = module {

    viewModelOf(::RecipeControlScreenViewModel)
    viewModelOf(::RecipeCategoriesSelectionBlockViewModel)

}
