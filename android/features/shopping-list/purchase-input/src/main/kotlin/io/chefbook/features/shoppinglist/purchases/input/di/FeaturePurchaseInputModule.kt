package io.chefbook.features.shoppinglist.purchases.input.di

import io.chefbook.features.shoppinglist.purchases.input.ui.PurchaseInputDialogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featurePurchaseInputModule() = module {
    viewModelOf(::PurchaseInputDialogViewModel)
}
