package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.repositories.SyncRepository
import com.cactusknights.chefbook.repositories.sync.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindAuthRepository(repository: SyncRepository): AuthRepository

    @Binds
    fun bindUserRepository(repository: SyncRepository): UserRepository

    @Binds
    fun bindRecipesRepository(repository: SyncRepository): RecipesRepository

    @Binds
    fun bindCategoriesRepository(repository: SyncRepository): CategoriesRepository

    @Binds
    fun bindShoppingListRepository(repository: SyncRepository): ShoppingListRepository

    @Binds
    fun bindEncryptionRepository(repository: SyncRepository): EncryptionRepository
}