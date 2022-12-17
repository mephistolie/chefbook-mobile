package com.mysty.chefbook.api.category.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity

@Dao
internal interface CategoryDao {

    @Query("SELECT * FROM ${CategoryRoomEntity.TABLE_NAME}")
    suspend fun getCategories(): List<CategoryRoomEntity>

    @Query("SELECT * FROM ${CategoryRoomEntity.TABLE_NAME} WHERE category_id = :categoryId")
    suspend fun getCategory(categoryId: String): CategoryRoomEntity?

    @Insert(entity = CategoryRoomEntity::class)
    suspend fun addCategory(categoryEntity: CategoryRoomEntity): Long

    @Update(entity = CategoryRoomEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryEntity: CategoryRoomEntity)

    @Query("DELETE FROM ${CategoryRoomEntity.TABLE_NAME} WHERE category_id = :categoryId")
    suspend fun deleteCategory(categoryId: String)

    @Query("DELETE FROM ${CategoryRoomEntity.TABLE_NAME}")
    suspend fun clearCategories()

}
