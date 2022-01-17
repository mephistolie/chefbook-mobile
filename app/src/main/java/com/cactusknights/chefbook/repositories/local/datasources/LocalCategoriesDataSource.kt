package com.cactusknights.chefbook.repositories.local.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cactusknights.chefbook.domain.CategoriesDataSource
import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.repositories.local.dao.CategoriesDao
import com.cactusknights.chefbook.repositories.local.entities.toCategory
import com.cactusknights.chefbook.repositories.local.entities.toCategoryEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.lang.reflect.Type
import javax.inject.Inject

class LocalCategoriesDataSource @Inject constructor(
    private val dao: CategoriesDao,
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
): CategoriesDataSource {

    companion object {
        private val DELETED_CATEGORY_REMOTE_IDS = stringPreferencesKey("deleted_categories")
    }

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
            val deletedIds = getDeletedCategoryRemoteIds()
            deletedIds.add(category.remoteId!!)
            dataStore.edit { prefs ->
                prefs[DELETED_CATEGORY_REMOTE_IDS] = gson.toJson(deletedIds)
            }
        }
    }

    suspend fun getDeletedCategoryRemoteIds(): ArrayList<Int> {
        val type: Type = object : TypeToken<ArrayList<Int>>() {}.type
        val prefs = dataStore.data.first()
        return gson.fromJson(prefs[DELETED_CATEGORY_REMOTE_IDS]?: "[]", type)
    }
}