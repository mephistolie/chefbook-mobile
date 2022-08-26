package com.cactusknights.chefbook.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cactusknights.chefbook.data.dto.local.room.CategoryRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipeRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipesCategoriesRoom
import com.cactusknights.chefbook.data.room.dao.CategoriesDao
import com.cactusknights.chefbook.data.room.dao.RecipeBookDao
import com.cactusknights.chefbook.data.room.dao.RecipeInteractionDao

@Database(
    entities = [
        RecipeRoom::class, CategoryRoom::class, RecipesCategoriesRoom::class
    ], version = 1, exportSchema = true
)
abstract class ChefBookDatabase: RoomDatabase() {
    abstract fun recipeBookDao(): RecipeBookDao
    abstract fun recipeInteractionDao(): RecipeInteractionDao
    abstract fun categoriesDao(): CategoriesDao
}
