package com.mysty.chefbook.api.common.di

import android.content.Context
import androidx.room.Room
import com.mysty.chefbook.api.common.room.ChefBookDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {

    singleOf(::getDatabase)
    singleOf(::getRecipeBookDao)
    singleOf(::getRecipeInteractionDao)
    singleOf(::getCategoriesDao)
}

private fun getDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        ChefBookDatabase::class.java,
        "chefbook_database"
    ).build()

private fun getRecipeBookDao(db: ChefBookDatabase) = db.recipeBookDao()
private fun getRecipeInteractionDao(db: ChefBookDatabase) = db.recipeInteractionDao()
private fun getCategoriesDao(db: ChefBookDatabase) = db.categoriesDao()
