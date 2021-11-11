package com.cactusknights.chefbook.repositories.local.dao

import androidx.room.*
import com.cactusknights.chefbook.repositories.local.entities.RecipeEntity

@Dao
interface ChefBookDao {

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME}")
    suspend fun getRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: Int): RecipeEntity

    @Insert(entity = RecipeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipe(recipeEntity: RecipeEntity)

    @Update(entity = RecipeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun deleteRecipe(recipeId: Int)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET isFavourite = :favourite WHERE id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: Int, favourite: Boolean)
}