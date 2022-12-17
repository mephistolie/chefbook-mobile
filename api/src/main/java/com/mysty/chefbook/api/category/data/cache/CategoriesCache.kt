package com.mysty.chefbook.api.category.data.cache

import com.mysty.chefbook.api.category.domain.entities.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal interface ICategoriesCacheReader {
    suspend fun observeCategories(): StateFlow<List<Category>?>
    suspend fun getCategories(): List<Category>
}

internal interface ICategoriesCacheWriter {
    suspend fun emitCategories(categories: List<Category>)
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun removeCategory(categoryId: String)
}

internal interface ICategoriesCache : ICategoriesCacheReader, ICategoriesCacheWriter

internal class CategoriesCache : ICategoriesCache {

    private val cachedList = MutableStateFlow<List<Category>?>(null)

    override suspend fun observeCategories(): StateFlow<List<Category>?> = cachedList.asStateFlow()

    override suspend fun getCategories(): List<Category> = cachedList.value.orEmpty()

    override suspend fun emitCategories(categories: List<Category>) {
        cachedList.emit(categories)
    }

    override suspend fun addCategory(category: Category) {
        val updatedCategories = cachedList.value?.filter { it.id != category.id }?.toMutableList()
        updatedCategories?.add(category)
        cachedList.emit(updatedCategories)
    }

    override suspend fun updateCategory(category: Category) {
        cachedList.emit(cachedList.value?.map { if (it.id != category.id) it else category })
    }

    override suspend fun removeCategory(categoryId: String) {
        cachedList.emit(cachedList.value?.filter { it.id != categoryId })
    }

}
