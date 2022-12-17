package com.mysty.chefbook.api.category.data.repository

import com.mysty.chefbook.api.category.data.cache.ICategoriesCache
import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.category.domain.entities.toCategory
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.SimpleAction
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.communication.data
import com.mysty.chefbook.api.common.communication.isFailure
import com.mysty.chefbook.api.common.communication.isSuccess
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import kotlin.math.abs
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class CategoryRepo(
    private val localSource: ILocalCategorySource,
    private val remoteSource: IRemoteCategorySource,

    private val cache: ICategoriesCache,
    private val source: ISourcesRepo,
    private val scopes: CoroutineScopes,
) : ICategoryRepo {

    private var refreshTimestamp: Long = 0

    override suspend fun observeCategories(): StateFlow<List<Category>?> {
        scopes.repository.launch { refreshData() }
        return cache.observeCategories()
    }

    override suspend fun getCategories(forceRefresh: Boolean): List<Category> {
        refreshData(forceRefresh)
        return cache.getCategories()
    }

    override suspend fun refreshCategories() {
        refreshData(forceRefresh = true)
    }

    private suspend fun refreshData(forceRefresh: Boolean = false) {
        if (source.useRemoteSource()) {
            if (forceRefresh || abs(System.currentTimeMillis() - refreshTimestamp) > REFRESH_TIME_THRESHOLD) {

                val localResult = localSource.getCategories()
                if (localResult.isSuccess()) cache.emitCategories(localResult.data())

                val remoteResult = remoteSource.getCategories()
                if (remoteResult.isSuccess()) {
                    cache.emitCategories(remoteResult.data())
                    pullChanges(if (localResult.isSuccess()) localResult.data() else emptyList(), remoteResult.data())
                }

                refreshTimestamp = System.currentTimeMillis()
            }
        } else {
            val localResult = localSource.getCategories()
            if (localResult.isSuccess()) cache.emitCategories(localResult.data())
        }
    }

    private suspend fun pullChanges(local: List<Category>, remote: List<Category>) {
        for (remoteCategory in remote) {
            val localCategory = local.find { it.id == remoteCategory.id }
            if (localCategory == null) {
                localSource.createCategory(remoteCategory)
            } else if (localCategory != remoteCategory) {
                localSource.updateCategory(remoteCategory)
            }
        }

        val remoteIds = remote.map { it.id }
        val deletedCategories = local.filter { it.id !in remoteIds }
        for (category in deletedCategories) {
            localSource.deleteCategory(category.id)
        }
    }

    override suspend fun createCategory(input: CategoryInput): ActionStatus<Category> {
        val result: ActionStatus<Category> = if (source.useRemoteSource()) {

            val remoteResult = remoteSource.createCategory(input)
            if (remoteResult.isSuccess()) {
                val newCategory = input.toCategory(id = remoteResult.data())
                localSource.createCategory(newCategory)
                DataResult(newCategory)
            } else remoteResult.asFailure()

        } else {

            val localResult = localSource.createCategory(input.toCategory())
            if (localResult.isSuccess()) {
                DataResult(input.toCategory(id = localResult.data()))
            } else localResult.asFailure()

        }

        if (result.isSuccess()) cache.addCategory(result.data())

        return result
    }

    override suspend fun getCategory(categoryId: String): ActionStatus<Category> {
        var result = localSource.getCategory(categoryId)
        if (result.isFailure() && source.useRemoteSource()) {
            result = remoteSource.getCategory(categoryId)
        }

        return result
    }

    override suspend fun updateCategory(categoryId: String, input: CategoryInput): ActionStatus<Category> {
        if (source.isOnlineMode()) {
            val result = remoteSource.updateCategory(categoryId, input)
            if (result.isFailure()) return result.asFailure()
        }

        val updatedCategory = input.toCategory(id = categoryId)
        localSource.updateCategory(updatedCategory)
        cache.updateCategory(updatedCategory)

        return DataResult(updatedCategory)
    }

    override suspend fun deleteCategory(categoryId: String): SimpleAction {
        if (source.isOnlineMode()) {
            val result = remoteSource.deleteCategory(categoryId)
            if (result.isFailure()) return result.asFailure()
        }

        localSource.deleteCategory(categoryId)
        cache.removeCategory(categoryId)
        return SuccessResult
    }

    companion object {
        private const val REFRESH_TIME_THRESHOLD = 5 * 60 * 1000
    }

}
