package com.cactusknights.chefbook.data.repositories

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.core.cache.CategoriesCacheManager
import com.cactusknights.chefbook.core.datastore.SettingsManager
import com.cactusknights.chefbook.data.CategoriesDataSource
import com.cactusknights.chefbook.data.LocalCategoriesDataSource
import com.cactusknights.chefbook.domain.CategoriesCrudRepo
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.di.Local
import com.cactusknights.chefbook.di.Remote
import javax.inject.Inject
import javax.inject.Singleton

class CategoriesCrudRepoImpl @Inject constructor(
    @Local private val localSource: LocalCategoriesDataSource,
    @Remote private val remoteSource: CategoriesDataSource,

    private val cache: CategoriesCacheManager,
    private val settings: SettingsManager
) : CategoriesCrudRepo {

    override suspend fun addCategory(category: Category): Int {
        category.id = localSource.addCategory(category)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            category.remoteId = remoteSource.addCategory(category)
            localSource.updateCategory(category)
        }
        cache.addCategory(category)
        return category.id!!
    }

    override suspend fun getCategory(categoryId: Int): Category {
        return localSource.getCategory(categoryId)
    }

    override suspend fun updateCategory(category: Category) {
        localSource.updateCategory(category)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE) {
            remoteSource.updateCategory(category)
        }
        cache.updateCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        val localId = category.id
        val remoteId = category.remoteId
        if (localId!= null) localSource.deleteCategory(localId)
        if (settings.getDataSourceType() == SettingsProto.DataSource.REMOTE && remoteId != null) {
            remoteSource.deleteCategory(remoteId)
        }
        cache.deleteCategory(category)
    }
}