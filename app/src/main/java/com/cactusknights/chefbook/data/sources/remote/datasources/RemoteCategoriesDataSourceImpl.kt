package com.cactusknights.chefbook.data.sources.remote.datasources

import com.cactusknights.chefbook.models.Category
import com.cactusknights.chefbook.data.CategoriesDataSource
import com.cactusknights.chefbook.data.sources.remote.api.CategoriesApi
import com.cactusknights.chefbook.data.sources.remote.dto.toCategory
import com.cactusknights.chefbook.data.sources.remote.dto.toCategoryDto
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class RemoteCategoriesDataSourceImpl @Inject constructor(
    private val api: CategoriesApi
) : CategoriesDataSource {
    override suspend fun getCategories(): ArrayList<Category> {
        val response = api.getCategories()
        val categoryDtos = response.body()
        if (categoryDtos != null) {
            val categories: ArrayList<Category> = arrayListOf()
            return categoryDtos.map { it.toCategory() }.toCollection(categories)
        } else throw IOException()
    }

    override suspend fun addCategory(category: Category) : Int {
        val response =  api.addCategory(category.toCategoryDto())
        return response.body()!!.id
    }

    override suspend fun getCategory(categoryId: Int): Category {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(category: Category) {
        api.updateCategory(category.remoteId.toString(), category.toCategoryDto())
    }

    override suspend fun deleteCategory(categoryId: Int) {
        api.deleteCategory(categoryId.toString())
    }
}