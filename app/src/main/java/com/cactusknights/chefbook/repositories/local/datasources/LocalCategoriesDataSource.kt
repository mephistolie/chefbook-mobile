package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.repositories.CategoriesDataSource
import com.cactusknights.chefbook.repositories.local.dao.CategoriesDao
import com.cactusknights.chefbook.repositories.local.entities.toCategory
import com.cactusknights.chefbook.repositories.local.entities.toCategoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCategoriesDataSource @Inject constructor(
    private val dao: CategoriesDao,
    private val syncData: DataStore<SyncDataProto>,
): CategoriesDataSource {

    override suspend fun getCategories(): ArrayList<Category> {
        val categories = arrayListOf<Category>()
        return dao.getCategories().map { it.toCategory() }.toCollection(categories)
    }

    override suspend fun addCategory(category: Category) : Int {
        return dao.addCategory(category.toCategoryEntity()).toInt()
    }

    override suspend fun getCategory(categoryId: Int): Category {
        return dao.getCategory(categoryId).toCategory()
    }

    override suspend fun updateCategory(category: Category) {
        return dao.updateCategory(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category.id!!)
        if (category.remoteId != null) {
            syncData.updateData { it.deletedCategoriesList.add(category.remoteId!!); it }
        }
    }

    suspend fun getDeletedCategoryRemoteIds(): List<Int> = syncData.data.take(1).first().deletedCategoriesList
}