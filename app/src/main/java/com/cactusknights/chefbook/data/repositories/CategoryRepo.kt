package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import com.cactusknights.chefbook.data.ILocalCategorySource
import com.cactusknights.chefbook.data.IRemoteCategorySource
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.SimpleAction
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.action.data
import com.cactusknights.chefbook.domain.entities.action.isFailure
import com.cactusknights.chefbook.domain.entities.action.isSuccess
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import com.cactusknights.chefbook.domain.entities.category.toCategory
import com.cactusknights.chefbook.domain.interfaces.ICategoriesCache
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

@Singleton
class CategoryRepo @Inject constructor(
    @Local private val localSource: ILocalCategorySource,
    @Remote private val remoteSource: IRemoteCategorySource,

    private val cache: ICategoriesCache,
    private val source: SourceRepo,
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

    override suspend fun getCategory(categoryId: Int): ActionStatus<Category> {
        var result = localSource.getCategory(categoryId)
        if (result.isFailure() && source.useRemoteSource()) {
            result = remoteSource.getCategory(categoryId)
        }

        return result
    }

    override suspend fun updateCategory(categoryId: Int, input: CategoryInput): ActionStatus<Category> {
        if (source.isOnlineMode()) {
            val result = remoteSource.updateCategory(categoryId, input)
            if (result.isFailure()) return result.asFailure()
        }

        val updatedCategory = input.toCategory(id = categoryId)
        localSource.updateCategory(updatedCategory)
        cache.updateCategory(updatedCategory)

        return DataResult(updatedCategory)
    }

    override suspend fun deleteCategory(categoryId: Int): SimpleAction {
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
