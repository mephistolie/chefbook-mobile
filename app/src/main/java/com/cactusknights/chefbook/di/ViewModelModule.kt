package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.ui.screens.auth.AuthViewModel
import com.cactusknights.chefbook.ui.screens.category.CategoryScreenViewModel
import com.cactusknights.chefbook.ui.screens.encryptedvault.EncryptedVaultScreenViewModel
import com.cactusknights.chefbook.ui.screens.favourite.FavouriteScreenViewModel
import com.cactusknights.chefbook.ui.screens.home.HomeViewModel
import com.cactusknights.chefbook.ui.screens.main.AppViewModel
import com.cactusknights.chefbook.ui.screens.recipe.RecipeScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipebook.RecipeBookScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.search.RecipeBookSearchScreenViewModel
import com.cactusknights.chefbook.ui.screens.shoppinglist.ShoppingListScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EncryptedVaultScreenViewModel)
    viewModelOf(::RecipeBookScreenViewModel)
    viewModelOf(::ShoppingListScreenViewModel)
    viewModelOf(::RecipeBookSearchScreenViewModel)
    viewModelOf(::FavouriteScreenViewModel)
    viewModelOf(::CategoryScreenViewModel)
    viewModelOf(::RecipeScreenViewModel)
    viewModelOf(::RecipeInputScreenViewModel)
}
