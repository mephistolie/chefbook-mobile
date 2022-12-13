package com.cactusknights.chefbook.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cactusknights.chefbook.data.dto.local.room.CategoryRoom

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM ${CategoryRoom.TABLE_NAME}")
    suspend fun getCategories(): List<CategoryRoom>

    @Query("SELECT * FROM ${CategoryRoom.TABLE_NAME} WHERE category_id = :categoryId")
    suspend fun getCategory(categoryId: String): CategoryRoom?

    @Insert(entity = CategoryRoom::class)
    suspend fun addCategory(categoryEntity: CategoryRoom): Long

    @Update(entity = CategoryRoom::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryEntity: CategoryRoom)

    @Query("DELETE FROM ${CategoryRoom.TABLE_NAME} WHERE category_id = :categoryId")
    suspend fun deleteCategory(categoryId: String)

    @Query("DELETE FROM ${CategoryRoom.TABLE_NAME}")
    suspend fun clearCategories()
}
