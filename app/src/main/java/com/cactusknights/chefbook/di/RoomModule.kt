package com.cactusknights.chefbook.di

import android.content.Context
import androidx.room.Room
import com.cactusknights.chefbook.core.room.ChefBookDatabase
import com.cactusknights.chefbook.repositories.local.dao.CategoriesDao
import com.cactusknights.chefbook.repositories.local.dao.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ChefBookDatabase::class.java,
            "chefbook_database"
        ).build()

    @Provides
    @Singleton
    fun provideRecipesDao(roomDatabase: ChefBookDatabase) : RecipesDao = roomDatabase.recipesDao()

    @Provides
    @Singleton
    fun provideCategoriesDao(roomDatabase: ChefBookDatabase) : CategoriesDao = roomDatabase.categoriesDao()
}