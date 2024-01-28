package io.chefbook.features.encryption.di

import io.chefbook.features.encryption.ui.vault.EncryptedVaultScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureEncryptionModule = module {
    viewModelOf(::EncryptedVaultScreenViewModel)
}
