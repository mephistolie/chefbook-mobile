package com.cactusknights.chefbook.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.cactusknights.chefbook.data.dto.local.room.CategoryRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipeRoom
import com.cactusknights.chefbook.data.dto.local.room.RecipesCategoriesRoom

@Dao
interface RecipeInteractionDao {

    @Query("SELECT * FROM ${CategoryRoom.TABLE_NAME} WHERE category_id IN (SELECT category_id FROM ${RecipesCategoriesRoom.TABLE_NAME} WHERE recipe_id = :recipeId)")
    suspend fun getRecipeCategories(recipeId: String): List<CategoryRoom>

    @Query("DELETE FROM ${RecipesCategoriesRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeCategories(recipeId: String)

    @Query("INSERT INTO ${RecipesCategoriesRoom.TABLE_NAME} (recipe_id, category_id) SELECT ${RecipeRoom.TABLE_NAME}.recipe_id, " +
            "${CategoryRoom.TABLE_NAME}.category_id FROM ${CategoryRoom.TABLE_NAME} LEFT JOIN ${RecipeRoom.TABLE_NAME} " +
            "ON ${RecipeRoom.TABLE_NAME}.recipe_id = :recipeId WHERE ${CategoryRoom.TABLE_NAME}.category_id IN (:categories)")
    suspend fun addRecipeCategories(recipeId : String, categories: List<String>)

    @Transaction
    suspend fun setRecipeCategories(recipeId : String, categories: List<String>) {
        deleteRecipeCategories(recipeId)
        addRecipeCategories(recipeId, categories)
    }

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET favourite = :favourite WHERE recipe_id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: String, favourite: Boolean)

    @Query("SELECT liked FROM ${RecipeRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun getRecipeLikedStatus(recipeId: String): Boolean

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET liked = :liked WHERE recipe_id = :recipeId")
    suspend fun setRecipeLikedStatus(recipeId: String, liked: Boolean)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = :likes WHERE recipe_id = :recipeId")
    suspend fun setLikes(recipeId: String, likes: Int?)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = likes + 1 WHERE recipe_id = :recipeId")
    suspend fun increaseRecipeLikes(recipeId: String)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = likes - 1 WHERE recipe_id = :recipeId")
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
