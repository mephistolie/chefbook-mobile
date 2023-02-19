package com.mysty.chefbook.api.recipe.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeCategoryRoomEntity
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeRoomEntity

@Dao
internal interface RecipeInteractionDao {

    @Query("SELECT * FROM ${CategoryRoomEntity.TABLE_NAME} WHERE category_id IN (SELECT category_id FROM ${RecipeCategoryRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId)")
    suspend fun getRecipeCategories(recipeId: String): List<CategoryRoomEntity>

    @Query("DELETE FROM ${RecipeCategoryRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeCategories(recipeId: String)

    @Query("INSERT INTO ${RecipeCategoryRoomEntity.TABLE_NAME} (recipe_id, category_id) SELECT ${RecipeRoomEntity.TABLE_NAME}.recipe_id, " +
            "${CategoryRoomEntity.TABLE_NAME}.category_id FROM ${CategoryRoomEntity.TABLE_NAME} LEFT JOIN ${RecipeRoomEntity.TABLE_NAME} " +
            "ON ${RecipeRoomEntity.TABLE_NAME}.recipe_id = :recipeId WHERE ${CategoryRoomEntity.TABLE_NAME}.category_id IN (:categories)")
    suspend fun addRecipeCategories(recipeId : String, categories: List<String>)

    @Transaction
    suspend fun setRecipeCategories(recipeId : String, categories: List<String>) {
        deleteRecipeCategories(recipeId)
        addRecipeCategories(recipeId, categories)
    }

    @Query("UPDATE ${RecipeRoomEntity.TABLE_NAME} SET favourite = :favourite WHERE recipe_id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: String, favourite: Boolean)

    @Query("SELECT liked FROM ${RecipeRoomEntity.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun getRecipeLikedStatus(recipeId: String): Boolean

    @Query("UPDATE ${RecipeRoomEntity.TABLE_NAME} SET liked = :liked WHERE recipe_id = :recipeId")
    suspend fun setRecipeLikedStatus(recipeId: String, liked: Boolean)

    @Query("UPDATE ${RecipeRoomEntity.TABLE_NAME} SET likes = :likes WHERE recipe_id = :recipeId")
    suspend fun setLikes(recipeId: String, likes: Int?)

    @Query("UPDATE ${RecipeRoomEntity.TABLE_NAME} SET likes = likes + 1 WHERE recipe_id = :recipeId")
    suspend fun increaseRecipeLikes(recipeId: String)

    @Query("UPDATE ${RecipeRoomEntity.TABLE_NAME} SET likes = likes - 1 WHERE recipe_id = :recipeId")
    suspend fun reduceRecipeLikes(recipeId: String)

    @Transaction
    suspend fun setRecipeLiked(recipeId : String, liked: Boolean) {
        val likedBefore = getRecipeLikedStatus(recipeId)
        if (likedBefore != liked) {
            setRecipeLikedStatus(recipeId, liked)
            if (liked) {
                increaseRecipeLikes(recipeId)
            } else {
                reduceRecipeLikes(recipeId)
            }
        }
    }

}
