package com.cactusknights.chefbook.di

import android.content.SharedPreferences
import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.models.Settings
import com.cactusknights.chefbook.repositories.*
import com.cactusknights.chefbook.repositories.local.datasources.*
import com.cactusknights.chefbook.repositories.remote.datasources.*
import com.cactusknights.chefbook.repositories.sync.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepositoryManager(
        rds: RemoteAuthDataSource,
        sr: SyncSettingsRepository
    ): SyncAuthRepository {
        return SyncAuthRepository(rds, sr)
    }

    @Provides
    @Singleton
    fun provideUsersRepositoryManager(
        lds: LocalUsersDataSource, rds: RemoteUserDataSource,
        sr: SyncSettingsRepository
    ): SyncUsersRepository {
        return SyncUsersRepository(lds, rds, sr)
    }

    @Provides
    @Singleton
    fun provideEncryptionRepositoryManager(lds: LocalEncryptionDataSource, rds: RemoteEncryptionDataSource
    ): SyncEncryptionRepository {
        return SyncEncryptionRepository(lds, rds)
    }

    @Provides
    @Singleton
    fun provideRecipesRepositoryManager(
        lds: LocalRecipesDataSource, rds: RemoteRecipesDataSource,
        cr: SyncCategoriesRepository, sr: SyncSettingsRepository
    ): SyncRecipesRepository {
        return SyncRecipesRepository(lds, rds, cr, sr)
    }

    @Provides
    @Singleton
    fun provideCategoriesRepositoryManager(
        lds: LocalCategoriesDataSource,
        rds: RemoteCategoriesDataSource,
        sr: SyncSettingsRepository
    ): SyncCategoriesRepository {
        return SyncCategoriesRepository(lds, rds, sr)
    }

    @Provides
    @Singleton
    fun provideShoppingListRepositoryManager(
        lds: LocalShoppingListDataSource,
        rds: RemoteShoppingListDataSource, sr: SyncSettingsRepository
    ): SyncShoppingListRepository {
        return SyncShoppingListRepository(lds, rds, sr)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(repository: SyncRepository): AuthRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideUserRepository(repository: SyncRepository): UserRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(repository: SyncRepository): RecipesRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(repository: SyncRepository): CategoriesRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideShoppingListRepository(repository: SyncRepository): ShoppingListRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(repository: SyncRepository): SettingsRepository {
        return repository
    }
}