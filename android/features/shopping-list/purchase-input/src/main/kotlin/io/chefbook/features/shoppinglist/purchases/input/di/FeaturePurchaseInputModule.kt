package io.chefbook.features.shoppinglist.purchases.input.di

import io.chefbook.features.shoppinglist.purchases.input.ui.PurchaseInputDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featurePurchaseInputModule = module {
    viewModelOf(::PurchaseInputDialogViewModel)
}
