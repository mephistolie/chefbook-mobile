package com.mysty.chefbook.api.category.domain

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction
import kotlinx.coroutines.flow.Flow

internal interface ICategoryRepo {
    fun observeCategories(): Flow<List<Category>?>
    suspend fun getCategories(forceRefresh: Boolean = false): List<Category>
    suspend fun refreshCategories()
    suspend fun createCategory(input: CategoryInput): ActionStatus<Category>
    suspend fun getCategory(categoryId: String): ActionStatus<Category>
    suspend fun updateCategory(categoryId: String, input: CategoryInput): ActionStatus<Category>
    suspend fun deleteCategory(categoryId: String): SimpleAction
}
