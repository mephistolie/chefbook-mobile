package com.cactusknights.chefbook.repositories.local.dao

import androidx.room.*
import com.cactusknights.chefbook.repositories.local.entities.CategoryEntity
import com.cactusknights.chefbook.repositories.local.entities.RecipeEntity
import com.cactusknights.chefbook.repositories.local.entities.RecipesCategoriesEntity

@Dao
interface RecipesDao {

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME}")
    suspend fun getRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE remoteId = :recipeId")
    suspend fun getRecipeByRemoteId(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE id IN (SELECT categoryId FROM ${RecipesCategoriesEntity.TABLE_NAME} WHERE recipeId = :recipeId)")
    suspend fun getRecipeCategories(recipeId: Int): List<CategoryEntity>

    @Query("DELETE FROM ${RecipesCategoriesEntity.TABLE_NAME} WHERE recipeId = :recipeId")
    suspend fun deleteRecipeCategories(recipeId: Int)

    @Query("INSERT INTO ${RecipesCategoriesEntity.TABLE_NAME} (recipeId, categoryId) SELECT ${RecipeEntity.TABLE_NAME}.id, ${CategoryEntity.TABLE_NAME}.id FROM ${CategoryEntity.TABLE_NAME} LEFT JOIN ${RecipeEntity.TABLE_NAME} ON ${RecipeEntity.TABLE_NAME}.id = :recipeId WHERE ${CategoryEntity.TABLE_NAME}.id IN (:categories)")
    suspend fun addRecipeCategories(recipeId : Int, categories: List<Int>)

    @Transaction
    suspend fun setRecipeCategories(recipeId : Int, categories: ArrayList<Int>) {
        deleteRecipeCategories(recipeId)
        addRecipeCategories(recipeId, categories)
    }

    @Insert(entity = RecipeEntity::class)
    suspend fun addRecipe(recipeEntity: RecipeEntity): Long

    @Update(entity = RecipeEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM ${RecipeEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun deleteRecipe(recipeId: Int)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET isFavourite = :favourite WHERE id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: Int, favourite: Boolean)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET isLiked = :liked, likes = :likes WHERE id = :recipeId")
    suspend fun setRecipeLikes(recipeId: Int, liked: Boolean, likes: Int)
}