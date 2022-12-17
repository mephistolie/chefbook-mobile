package com.mysty.chefbook.api.common.di

import com.mysty.chefbook.api.common.crypto.HybridCryptor
import com.mysty.chefbook.api.common.crypto.ICryptor
import com.mysty.chefbook.api.common.crypto.IHybridCryptor
import com.mysty.chefbook.api.common.files.FileManager
import com.mysty.chefbook.api.common.files.IFileManager
import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val coreDataModule = module {

    single(createdAtStart = true) { AppDispatchers() }
    singleOf(::CoroutineScopes) { createdAtStart() }

    singleOf(::HybridCryptor) binds arrayOf(IHybridCryptor::class, ICryptor::class)

    singleOf(::FileManager) bind IFileManager::class

}
