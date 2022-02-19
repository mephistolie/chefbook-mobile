package com.cactusknights.chefbook.data.sources.local.dao

import androidx.room.*
import com.cactusknights.chefbook.data.sources.local.entities.CategoryEntity

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME}")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM ${CategoryEntity.TABLE_NAME} WHERE id = :categoryId")
    suspend fun getCategory(categoryId: Int): CategoryEntity

    @Insert(entity = CategoryEntity::class)
    suspend fun addCategory(categoryEntity: CategoryEntity): Long

    @Update(entity = CategoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM ${CategoryEntity.TABLE_NAME} WHERE id = :categoryId")
    suspend fun deleteCategory(categoryId: Int)
}