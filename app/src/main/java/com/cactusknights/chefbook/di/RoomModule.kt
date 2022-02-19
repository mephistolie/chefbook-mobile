package com.cactusknights.chefbook.di

import android.content.Context
import androidx.room.Room
import com.cactusknights.chefbook.core.room.ChefBookDatabase
import com.cactusknights.chefbook.data.sources.local.dao.CategoriesDao
import com.cactusknights.chefbook.data.sources.local.dao.RecipeBookDao
import com.cactusknights.chefbook.data.sources.local.dao.RecipeInteractionDao
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
    fun provideRecipeBookDao(roomDatabase: ChefBookDatabase) : RecipeBookDao = roomDatabase.recipeBookDao()

    @Provides
    @Singleton
    fun provideRecipeInteractionDao(roomDatabase: ChefBookDatabase) : RecipeInteractionDao = roomDatabase.recipeInteractionDao()

    @Provides
    @Singleton
    fun provideCategoriesDao(roomDatabase: ChefBookDatabase) : CategoriesDao = roomDatabase.categoriesDao()
}