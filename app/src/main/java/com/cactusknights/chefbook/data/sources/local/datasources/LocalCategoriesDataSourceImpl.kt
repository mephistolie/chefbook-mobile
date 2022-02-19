package com.cactusknights.chefbook.data.sources.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.data.CategoriesDataSource
import com.cactusknights.chefbook.data.LocalCategoriesDataSource
import com.cactusknights.chefbook.data.sources.local.dao.CategoriesDao
import com.cactusknights.chefbook.data.sources.local.entities.toCategory
import com.cactusknights.chefbook.data.sources.local.entities.toCategoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCategoriesDataSourceImpl @Inject constructor(
    private val dao: CategoriesDao,
    private val syncData: DataStore<SyncDataProto>,
): LocalCategoriesDataSource {

    override suspend fun getCategories(): List<Category> = dao.getCategories().map { it.toCategory() }

    override suspend fun addCategory(category: Category) : Int = dao.addCategory(category.toCategoryEntity()).toInt()

    override suspend fun getCategory(categoryId: Int): Category = dao.getCategory(categoryId).toCategory()

    override suspend fun updateCategory(category: Category) = dao.updateCategory(category.toCategoryEntity())

    override suspend fun deleteCategory(categoryId: Int) {
        val remoteId = getCategory(categoryId).remoteId
        dao.deleteCategory(categoryId)
        if (remoteId != null) syncData.updateData {
            it.toBuilder()
                .addDeletedCategories(remoteId)
                .build()
        }
    }

    override suspend fun getDeletedCategoriesRemoteIds(): List<Int> = syncData.data.take(1).first().deletedCategoriesList

}