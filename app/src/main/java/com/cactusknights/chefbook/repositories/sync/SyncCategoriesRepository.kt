package com.cactusknights.chefbook.repositories.sync

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.domain.CategoriesRepository
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.repositories.local.datasources.LocalCategoriesDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteCategoriesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class SyncCategoriesRepository @Inject constructor(
    private val localSource: LocalCategoriesDataSource,
    private val remoteSource: RemoteCategoriesDataSource,
    private val settings: SettingsManager
) : CategoriesRepository {

    companion object {
        const val SYNC_TIMEOUT = 60000
    }

    private val categories: MutableStateFlow<List<Category>?> = MutableStateFlow(null)

    private var syncTimestamp : Long? = null
    private val mutex = Mutex()

    override suspend fun listenToCategories(): StateFlow<List<Category>?> {
        mutex.withLock {
            if (syncTimestamp == null || abs(System.currentTimeMillis() - syncTimestamp!!) > SyncRecipesRepository.SYNC_TIMEOUT) {
                CoroutineScope(Dispatchers.IO).launch { getCategories() }
                syncTimestamp = System.currentTimeMillis()
            }
        }
        return categories
    }

    override suspend fun getCategories(): List<Category> {
        val localCategories = localSource.getCategories(); categories.emit(localCategories)

        return if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            try {
                val remoteCategories = remoteSource.getCategories()
                val preparedRemoteCategories = setCategoryId(localCategories, remoteCategories)
                categories.emit(preparedRemoteCategories)
                CoroutineScope(Dispatchers.IO).launch { syncCategories(localCategories, preparedRemoteCategories) }
                preparedRemoteCategories
            } catch (e: Exception) { localCategories }
        } else { localCategories }
    }

    override suspend fun addCategory(category: Category): Int {
        category.id = localSource.addCategory(category);
        val newCategories = arrayListOf<Category>()
        val currentCategories = categories.value?: arrayListOf()
        newCategories.addAll(currentCategories); newCategories.add(category)
        categories.emit(newCategories)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            category.remoteId = remoteSource.addCategory(category)
            localSource.updateCategory(category)
        }
        return category.id!!
    }

    override suspend fun getCategory(categoryId: Int): Category {
        return localSource.getCategory(categoryId)
    }

    override suspend fun updateCategory(category: Category) {
        localSource.updateCategory(category)
        categories.emit(categories.value?.map { if (it.id == category.id) category else it })
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            remoteSource.updateCategory(category)
        }
    }

    override suspend fun deleteCategory(category: Category) {
        localSource.deleteCategory(category)
        categories.emit(categories.value?.filter { it.id != category.id || it.remoteId != category.remoteId })
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            remoteSource.deleteCategory(category)
        }
    }

    private fun setCategoryId(localData: ArrayList<Category>, remoteData: ArrayList<Category>) : ArrayList<Category> {
        remoteData.map { remote -> remote.id = localData.filter { local -> local.remoteId == remote.remoteId}.getOrNull(0)?.id }
        return remoteData
    }

    private suspend fun syncCategories(localData: List<Category>, remoteData: List<Category>) {
        val syncedCategories = arrayListOf<Category>()
        var filteredRemoteCategories = remoteData

        for (localCategory in localData) {

            // Send category from local source to remote source
            if (localCategory.remoteId == null) {
                localCategory.remoteId = remoteSource.addCategory(localCategory)
                localSource.updateCategory(localCategory)
                syncedCategories.add(localCategory); continue
            }

            val remoteCategory = filteredRemoteCategories.filter { it.remoteId == localCategory.remoteId }.getOrNull(0)

            // Remove deleted category from local source
            if (remoteCategory == null) { localSource.deleteCategory(localCategory); continue }

            if (localCategory.name != remoteCategory.name || localCategory.cover != remoteCategory.cover) {
                localSource.updateCategory(remoteCategory)
            }

            syncedCategories.add(localCategory)
            filteredRemoteCategories = filteredRemoteCategories.filter { it.remoteId != localCategory.remoteId } as ArrayList<Category>
        }

        val deletedIds = localSource.getDeletedCategoryRemoteIds()
        for (remoteCategory in filteredRemoteCategories) {
            if (remoteCategory.remoteId in deletedIds) remoteSource.deleteCategory(remoteCategory)
            else {
                remoteCategory.id = localSource.addCategory(remoteCategory); syncedCategories.add(remoteCategory)
            }
        }

        categories.emit(syncedCategories)
    }
}