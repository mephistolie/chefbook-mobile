package com.mysty.chefbook.api.recipe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeCategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeRoomEntity

@Dao
internal interface RecipeBookDao {

    @Query("SELECT * FROM ${RecipeRoomEntity.TABLE_NAME}")
    suspend fun getRecipes(): List<RecipeRoomEntity>

    @Query("SELECT * FROM ${RecipeRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun getRecipeById(recipeId: String): RecipeRoomEntity?

    @Query("SELECT * FROM ${CategoryRoomEntity.TABLE_NAME} WHERE category_id IN (SELECT category_id FROM ${RecipeCategoryRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId)")
    suspend fun getRecipeCategories(recipeId: String): List<CategoryRoomEntity>

    @Insert(entity = RecipeRoomEntity::class)
    suspend fun addRecipe(recipeEntity: RecipeRoomEntity)

    @Update(entity = RecipeRoomEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecipe(recipeEntity: RecipeRoomEntity)

    @Query("DELETE FROM ${RecipeRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun deleteRecipe(recipeId: String)
}
