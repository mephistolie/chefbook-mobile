package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.core.encryption.HybridCryptor
import com.cactusknights.chefbook.core.encryption.IHybridCryptor
import com.cactusknights.chefbook.domain.interfaces.ICryptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class EncryptionModule {

    @Provides
    fun provideEncryptionManager(): IHybridCryptor = HybridCryptor()

}

@Module
@InstallIn(SingletonComponent::class)
interface EncryptionBindModule {

    @Binds
    fun bindEncryptionManagerToCryptor(cryptor: IHybridCryptor): ICryptor

}
