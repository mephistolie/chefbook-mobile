package com.mysty.chefbook.features.recipe.input.di

import com.mysty.chefbook.features.recipe.input.ui.viewmodel.RecipeInputScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureRecipeInputModule = module {
    viewModel { (recipeId: String?) ->
        RecipeInputScreenViewModel(recipeId = recipeId, get(), get(), get(), get(), get(), get(), get(), get())
    }
}
