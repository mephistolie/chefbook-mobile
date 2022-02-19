package com.cactusknights.chefbook.core.cache

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.models.Recipe
import com.cactusknights.chefbook.models.RecipeInfo
import com.cactusknights.chefbook.models.info
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

interface CategoriesCacheReader {
    suspend fun listenToCategories(): StateFlow<List<Category>?>
    suspend fun getCategories() : List<Category>
}

interface CategoriesCacheWriter {
    suspend fun setCategories(categories: List<Category>)
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

interface CategoriesCacheManager : CategoriesCacheReader, CategoriesCacheWriter

class CategoriesCacheManagerImpl : CategoriesCacheManager {

    private val categoriesCache: MutableStateFlow<ArrayList<Category>?> = MutableStateFlow(null)

    override suspend fun listenToCategories(): StateFlow<List<Category>?> = categoriesCache
    override suspend fun getCategories(): List<Category> = categoriesCache.value?: emptyList()

    override suspend fun setCategories(categories: List<Category>) = categoriesCache.emit(categories as ArrayList<Category>)

    override suspend fun addCategory(category: Category) {
        val currentCategories = categoriesCache.value
        currentCategories?.add(category)
        categoriesCache.emit(currentCategories)
    }

    override suspend fun updateCategory(category: Category) {
        val currentCategories = categoriesCache.value
        val updatedCategories = currentCategories?.map { if (sameId(it.id, category.id) || sameId(it.remoteId, category.remoteId)) category else it} as ArrayList<Category>
        categoriesCache.emit(updatedCategories)
    }

    override suspend fun deleteCategory(category: Category) {
        val currentCategories = categoriesCache.value
        val filteredCategories = currentCategories?.filter { !sameId(it.id, category.id) && !sameId(it.remoteId, category.remoteId) } as ArrayList<Category>
        categoriesCache.emit(filteredCategories)
    }

    private fun sameId(first: Int?, second: Int?) : Boolean = first == second && first != null
}