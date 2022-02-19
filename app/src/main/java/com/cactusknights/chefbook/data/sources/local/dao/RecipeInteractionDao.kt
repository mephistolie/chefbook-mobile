package com.cactusknights.chefbook.data.sources.local.dao

import androidx.room.*
import com.cactusknights.chefbook.core.room.RoomConverters
import com.cactusknights.chefbook.data.sources.local.entities.CategoryEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipeEntity
import com.cactusknights.chefbook.data.sources.local.entities.RecipesCategoriesEntity
import java.util.*

@Dao
@TypeConverters(RoomConverters::class)
interface RecipeInteractionDao {

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE id IN (SELECT categoryId FROM ${RecipesCategoriesEntity.TABLE_NAME} WHERE recipeId = :recipeId)")
    suspend fun getRecipeCategories(recipeId: Int): List<CategoryEntity>

    @Query("DELETE FROM ${RecipesCategoriesEntity.TABLE_NAME} WHERE recipeId = :recipeId")
    suspend fun deleteRecipeCategories(recipeId: Int)

    @Query("INSERT INTO ${RecipesCategoriesEntity.TABLE_NAME} (recipeId, categoryId) SELECT ${RecipeEntity.TABLE_NAME}.id, ${CategoryEntity.TABLE_NAME}.id FROM ${CategoryEntity.TABLE_NAME} LEFT JOIN ${RecipeEntity.TABLE_NAME} ON ${RecipeEntity.TABLE_NAME}.id = :recipeId WHERE ${CategoryEntity.TABLE_NAME}.id IN (:categories)")
    suspend fun addRecipeCategories(recipeId : Int, categories: List<Int>)

    @Transaction
    suspend fun setRecipeCategories(recipeId : Int, categories: List<Int>) {
        deleteRecipeCategories(recipeId)
        addRecipeCategories(recipeId, categories)
    }

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET isFavourite = :favourite WHERE id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: Int, favourite: Boolean)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET isLiked = :liked WHERE id = :recipeId")
    suspend fun setRecipeLiked(recipeId: Int, liked: Boolean)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET likes = likes + 1 WHERE id = :recipeId")
    suspend fun increaseLikes(recipeId: Int)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET likes = likes - 1 WHERE id = :recipeId")
    suspend fun reduceLikes(recipeId: Int)

    @Query("UPDATE ${RecipeEntity.TABLE_NAME} SET userTimestamp = :timestamp WHERE id = :recipeId")
    suspend fun updateRecipeInteractionTimestamp(recipeId: Int, timestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis))
}