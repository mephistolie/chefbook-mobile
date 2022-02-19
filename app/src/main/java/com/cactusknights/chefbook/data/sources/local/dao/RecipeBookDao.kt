package com.cactusknights.chefbook.data.sources.local.dao

import androidx.room.*
import com.cactusknights.chefbook.data.sources.local.entities.CategoryEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipeEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipesCategoriesEntity

@Dao
interface RecipeBookDao {

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME}")
    suspend fun getRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE remoteId = :recipeId")
    suspend fun getRecipeByRemoteId(recipeId: Int): RecipeEntity?

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE id IN (SELECT categoryId FROM ${RecipesCategoriesEntity.TABLE_NAME} WHERE recipeId = :recipeId)")
    suspend fun getRecipeCategories(recipeId: Int): List<CategoryEntity>

    @Insert(entity = RecipeEntity::class)
    suspend fun addRecipe(recipeEntity: RecipeEntity): Long

    @Update(entity = RecipeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun deleteRecipe(recipeId: Int)
}