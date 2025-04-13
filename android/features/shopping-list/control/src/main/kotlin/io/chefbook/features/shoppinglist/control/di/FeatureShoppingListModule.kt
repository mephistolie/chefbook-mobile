package io.chefbook.features.shoppinglist.control.di

import io.chefbook.features.shoppinglist.control.ui.screen.ShoppingListScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureShoppingListModule() = module {
    viewModelOf(::ShoppingListScreenViewModel)
}
