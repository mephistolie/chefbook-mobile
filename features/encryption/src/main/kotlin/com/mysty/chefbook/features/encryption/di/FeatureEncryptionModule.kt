package com.mysty.chefbook.features.encryption.di

import com.mysty.chefbook.features.encryption.ui.vault.EncryptedVaultScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureEncryptionModule = module {
    viewModelOf(::EncryptedVaultScreenViewModel)
}
