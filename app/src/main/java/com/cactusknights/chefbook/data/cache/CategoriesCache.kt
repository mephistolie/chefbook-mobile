package com.cactusknights.chefbook.data.cache

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCache
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class CategoriesCache @Inject constructor() : ICategoriesCache {

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

    override suspend fun removeCategory(categoryId: Int) {
        cachedList.emit(cachedList.value?.filter { it.id != categoryId })
    }


}
