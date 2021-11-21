package com.cactusknights.chefbook.di

import android.content.SharedPreferences
import com.cactusknights.chefbook.ChefBookRepository
import com.cactusknights.chefbook.domain.*
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
    fun provideChefBookRepository(sp: SharedPreferences, lds: LocalDataSource, rds: RemoteDataSource): ChefBookRepository {
        return ChefBookRepository(lds, rds, sp)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(repository: ChefBookRepository): AuthRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideUserRepository(repository: ChefBookRepository): UserRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(repository: ChefBookRepository): RecipesRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideCategoriesRepository(repository: ChefBookRepository): CategoriesRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideShoppingListRepository(repository: ChefBookRepository): ShoppingListRepository {
        return repository
    }
}