package com.cactusknights.chefbook.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cactusknights.chefbook.repositories.local.dao.CategoriesDao
import com.cactusknights.chefbook.repositories.local.dao.RecipesDao
import com.cactusknights.chefbook.repositories.local.entities.CategoryEntity
import com.cactusknights.chefbook.repositories.local.entities.RecipeEntity
import com.cactusknights.chefbook.repositories.local.entities.RecipesCategoriesEntity

@Database(
    entities = [
        RecipeEntity::class, CategoryEntity::class, RecipesCategoriesEntity::class
    ], version = 1, exportSchema = true
)
abstract class ChefBookDatabase: RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
    abstract fun categoriesDao(): CategoriesDao
}