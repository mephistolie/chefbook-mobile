package com.cactusknights.chefbook.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cactusknights.chefbook.data.sources.local.dao.CategoriesDao
import com.cactusknights.chefbook.data.sources.local.dao.RecipeBookDao
import com.cactusknights.chefbook.data.sources.local.dao.RecipeInteractionDao
import com.cactusknights.chefbook.data.sources.local.entities.CategoryEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipeEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipesCategoriesEntity

@Database(
    entities = [
        RecipeEntity::class, CategoryEntity::class, RecipesCategoriesEntity::class
    ], version = 1, exportSchema = true
)
abstract class ChefBookDatabase: RoomDatabase() {
    abstract fun recipeBookDao(): RecipeBookDao
    abstract fun recipeInteractionDao(): RecipeInteractionDao
    abstract fun categoriesDao(): CategoriesDao
}