package com.mysty.chefbook.api.category.data.repository

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.SimpleAction

interface ICategorySource {
    suspend fun getCategories(): ActionStatus<List<Category>>
    suspend fun getCategory(categoryId: String): ActionStatus<Category>
    suspend fun deleteCategory(categoryId: String): SimpleAction
}

interface ILocalCategorySource : ICategorySource {
    suspend fun createCategory(category: Category): ActionStatus<String>
    suspend fun updateCategory(category: Category): SimpleAction
}

interface IRemoteCategorySource : ICategorySource {
    suspend fun createCategory(input: CategoryInput): ActionStatus<String>
    suspend fun updateCategory(categoryId: String, input: CategoryInput): SimpleAction
}
