package com.cactusknights.chefbook.data.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.cache.CategoriesCacheManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.data.CategoriesDataSource
import com.cactusknights.chefbook.data.LocalCategoriesDataSource
import com.cactusknights.chefbook.domain.CategoriesCrudRepo
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.CategoriesSyncRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesSyncRepoImpl @Inject constructor(
    @Local private val localSource: LocalCategoriesDataSource,
    @Remote private val remoteSource: CategoriesDataSource,

    private val cache: CategoriesCacheManager,
    private val settings: SettingsManager
) : CategoriesSyncRepo {

    override suspend fun listenToCategories(): StateFlow<List<Category>?> = cache.listenToCategories()
    override suspend fun getCategories(): List<Category> = cache.getCategories()

    override suspend fun syncCategories() {
        val localCategories = localSource.getCategories()
        cache.setCategories(localCategories)

        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            try {
                val remoteCategories = syncCategoriesId(localCategories, remoteSource.getCategories())
                cache.setCategories(remoteCategories)
                val syncedCategories = syncCategories(localCategories, remoteCategories)
                cache.setCategories(syncedCategories)
            } catch (e: Exception) {}
        }
    }

    private fun syncCategoriesId(localData: List<Category>, remoteData: List<Category>) : List<Category> {
        remoteData.map { remote -> remote.id = localData.filter { local -> local.remoteId == remote.remoteId}.getOrNull(0)?.id }
        return remoteData
    }

    private suspend fun syncCategories(localData: List<Category>, remoteData: List<Category>) : List<Category> {
        val syncedCategories = arrayListOf<Category>()
        var uncachedCategories = remoteData

        val pushedCategories = syncCachedCategories(localData, remoteData)
        val pushedCategoriesId = pushedCategories.map { it.remoteId }
        uncachedCategories = uncachedCategories.filter { it.remoteId !in pushedCategoriesId }
        syncedCategories.addAll(pushedCategories)

        val pulledCategories = syncUncachedCategories(uncachedCategories)
        syncedCategories.addAll(pulledCategories)

        return syncedCategories
    }

    private suspend fun syncCachedCategories(cachedCategories : List<Category>, uncachedCategories: List<Category>): List<Category> {
        val syncedCategories = arrayListOf<Category>()
        for (cachedCategory in cachedCategories) {
            val remoteCategory = uncachedCategories.filter { it.remoteId == cachedCategory.remoteId }.getOrNull(0)

            val syncedCategory = syncCachedCategory(cachedCategory, remoteCategory)
            if (syncedCategory != null) { syncedCategories.add(syncedCategory) }
        }
        return syncedCategories
    }

    private suspend fun syncCachedCategory(localCategory: Category, remoteCategory: Category? = null): Category? {
        val localId = localCategory.id
        val remoteId = localCategory.remoteId?: remoteCategory?.remoteId

        // Send category from local source to remote source
        if (remoteId == null) {
            localCategory.remoteId = remoteSource.addCategory(localCategory)
            localSource.updateCategory(localCategory)
            return localCategory
        }

        // Remove deleted category from local source
        if (remoteCategory == null) { localSource.deleteCategory(localCategory.id!!); return null }

        if (localCategory.name != remoteCategory.name || localCategory.cover != remoteCategory.cover) {
            localSource.updateCategory(remoteCategory)
        }

        remoteCategory.id = localId
        return remoteCategory
    }

    private suspend fun syncUncachedCategories(uncachedCategories: List<Category>) : List<Category> {
        val syncedCategories = arrayListOf<Category>()
        val deletedIds = localSource.getDeletedCategoriesRemoteIds()
        for (remoteCategory in uncachedCategories) {
            if (remoteCategory.remoteId in deletedIds) remoteSource.deleteCategory(remoteCategory.remoteId!!)
            else {
                remoteCategory.id = localSource.addCategory(remoteCategory); syncedCategories.add(remoteCategory)
            }
        }
        return syncedCategories
    }
}