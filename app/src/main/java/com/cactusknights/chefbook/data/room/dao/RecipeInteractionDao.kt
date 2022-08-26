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
    suspend fun getRecipeCategories(recipeId: Int): List<CategoryRoom>

    @Query("DELETE FROM ${RecipesCategoriesRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun deleteRecipeCategories(recipeId: Int)

    @Query("INSERT INTO ${RecipesCategoriesRoom.TABLE_NAME} (recipe_id, category_id) SELECT ${RecipeRoom.TABLE_NAME}.recipe_id, " +
            "${CategoryRoom.TABLE_NAME}.category_id FROM ${CategoryRoom.TABLE_NAME} LEFT JOIN ${RecipeRoom.TABLE_NAME} " +
            "ON ${RecipeRoom.TABLE_NAME}.recipe_id = :recipeId WHERE ${CategoryRoom.TABLE_NAME}.category_id IN (:categories)")
    suspend fun addRecipeCategories(recipeId : Int, categories: List<Int>)

    @Transaction
    suspend fun setRecipeCategories(recipeId : Int, categories: List<Int>) {
        deleteRecipeCategories(recipeId)
        addRecipeCategories(recipeId, categories)
    }

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET favourite = :favourite WHERE recipe_id = :recipeId")
    suspend fun setRecipeFavourite(recipeId: Int, favourite: Boolean)

    @Query("SELECT liked FROM ${RecipeRoom.TABLE_NAME} WHERE recipe_id = :recipeId")
    suspend fun getRecipeLikedStatus(recipeId: Int): Boolean

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET liked = :liked WHERE recipe_id = :recipeId")
    suspend fun setRecipeLikedStatus(recipeId: Int, liked: Boolean)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = :likes WHERE recipe_id = :recipeId")
    suspend fun setLikes(recipeId: Int, likes: Int?)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = likes + 1 WHERE recipe_id = :recipeId")
    suspend fun increaseRecipeLikes(recipeId: Int)

    @Query("UPDATE ${RecipeRoom.TABLE_NAME} SET likes = likes - 1 WHERE recipe_id = :recipeId")
    suspend fun reduceRecipeLikes(recipeId: Int)

    @Transaction
    suspend fun setRecipeLiked(recipeId : Int, liked: Boolean) {
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
