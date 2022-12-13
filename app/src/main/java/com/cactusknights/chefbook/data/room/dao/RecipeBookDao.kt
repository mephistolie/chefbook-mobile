package com.cactusknights.chefbook.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cactusknights.chefbook.data.dto.local.room.CategoryRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipeRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipesCategoriesRoom

@Dao
interface RecipeBookDao {

    @Query("SELECT * FROM ${RecipeRoom.TABLE_NAME}")
    suspend fun getRecipes(): List<RecipeRoom>

    @Query("SELECT * FROM ${RecipeRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun getRecipeById(recipeId: String): RecipeRoom?

    @Query("SELECT * FROM ${CategoryRoom.TABLE_NAME} WHERE category_id IN (SELECT category_id FROM ${RecipesCategoriesRoom.TABLE_NAME} WHERE recipe_id = :recipeId)")
    suspend fun getRecipeCategories(recipeId: String): List<CategoryRoom>

    @Insert(entity = RecipeRoom::class)
    suspend fun addRecipe(recipeEntity: RecipeRoom)

    @Update(entity = RecipeRoom::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecipe(recipeEntity: RecipeRoom)

    @Query("DELETE FROM ${RecipeRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun deleteRecipe(recipeId: String)
}
