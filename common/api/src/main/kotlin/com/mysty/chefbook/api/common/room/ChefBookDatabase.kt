package com.mysty.chefbook.api.common.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mysty.chefbook.api.category.data.local.dao.CategoryDao
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.dao.RecipeBookDao
import com.mysty.chefbook.api.recipe.data.local.dao.RecipeInteractionDao
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeCategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeRoomEntity

@Database(
    entities = [RecipeRoomEntity::class, CategoryRoomEntity::class, RecipeCategoryRoomEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class ChefBookDatabase : RoomDatabase() {
    abstract fun recipeBookDao(): RecipeBookDao
    abstract fun recipeInteractionDao(): RecipeInteractionDao
    abstract fun categoriesDao(): CategoryDao
}
