package com.cactusknights.chefbook.di

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.TokensProto
import com.cactusknights.chefbook.common.retrofit.CommonApi
import com.cactusknights.chefbook.core.cache.*
import com.cactusknights.chefbook.core.datastore.DataStoreSessionManager
import com.cactusknights.chefbook.core.datastore.DataStoreSettingsManager
import com.cactusknights.chefbook.core.datastore.SessionManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.core.encryption.Cryptor
import com.cactusknights.chefbook.core.encryption.EncryptionManager
import com.cactusknights.chefbook.core.encryption.EncryptionManagerImpl
import com.cactusknights.chefbook.core.retrofit.UriDataProvider
import com.cactusknights.chefbook.core.retrofit.UriDataProviderImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSettingsManager(settings: DataStore<SettingsProto>): SettingsManager = DataStoreSettingsManager(settings)

    @Provides
    @Singleton
    fun provideSessionManager(tokens: DataStore<TokensProto>): SessionManager = DataStoreSessionManager(tokens)

    @Provides
    fun provideEncryptionManager(): EncryptionManager = EncryptionManagerImpl()

    @Provides
    @Singleton
    fun provideRecipesCache(): RecipeBookCacheManager = RecipeBookCacheManagerImpl()

    @Provides
    @Singleton
    fun provideCategoriesCache(): CategoriesCacheManager = CategoriesCacheManagerImpl()

    @Provides
    fun provideDataProvider(api: CommonApi): UriDataProvider = UriDataProviderImpl(api)
}

@Module
@InstallIn(SingletonComponent::class)
interface AppBindModule {

    @Binds
    fun bindEncryptionManagerToCryptor(cryptor: EncryptionManager): Cryptor

    @Binds
    fun bindRecipeBookCacheReader(manager: RecipeBookCacheManager): RecipeBookCacheReader

    @Binds
    fun bindRecipeBookCacheWriter(manager: RecipeBookCacheManager): RecipeBookCacheWriter

    @Binds
    fun bindCategoriesCacheReader(manager: CategoriesCacheManager): CategoriesCacheReader

    @Binds
    fun bindCategoriesCacheWriter(manager: CategoriesCacheManager): CategoriesCacheWriter
}