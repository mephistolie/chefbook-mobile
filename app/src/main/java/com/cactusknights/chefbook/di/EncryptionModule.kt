package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.core.encryption.HybridCryptor
import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val encryptionModule = module {
    singleOf(::HybridCryptor) binds arrayOf(IHybridCryptor::class, ICryptor::class)
}
