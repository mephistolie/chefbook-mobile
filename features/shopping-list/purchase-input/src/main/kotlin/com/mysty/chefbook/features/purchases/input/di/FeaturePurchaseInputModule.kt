package com.mysty.chefbook.features.purchases.input.di

import com.mysty.chefbook.features.purchases.input.ui.PurchaseInputDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featurePurchaseInputModule = module {
    viewModelOf(::PurchaseInputDialogViewModel)
}
