package io.chefbook.features.encryption.di

import io.chefbook.features.encryption.ui.vault.EncryptedVaultScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun featureEncryptionModule() = module {
    viewModelOf(::EncryptedVaultScreenViewModel)
}
