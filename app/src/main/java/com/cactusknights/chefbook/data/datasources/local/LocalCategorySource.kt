package com.cactusknights.chefbook.data.datasources.local

import com.cactusknights.chefbook.data.ILocalCategorySource
import com.cactusknights.chefbook.data.dto.local.room.toEntity
import com.cactusknights.chefbook.data.dto.local.room.toRoom
import com.cactusknights.chefbook.data.room.RoomHandler
import com.cactusknights.chefbook.data.room.dao.CategoriesDao
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.AppError
import com.cactusknights.chefbook.domain.entities.action.AppErrorType
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.Failure
import com.cactusknights.chefbook.domain.entities.action.SuccessResult
import com.cactusknights.chefbook.domain.entities.category.Category
import javax.inject.Singleton

@Singleton
class LocalCategorySource(
    private val dao: CategoriesDao,
) : ILocalCategorySource {

    override suspend fun getCategories(): ActionStatus<List<Category>> =
        RoomHandler.handleQuery { DataResult(dao.getCategories().map { it.toEntity() }) }


    override suspend fun createCategory(category: Category) =
        RoomHandler.handleQuery {
            dao.addCategory(category.toRoom())
            SuccessResult
        }

    override suspend fun getCategory(categoryId: String) =
        RoomHandler.handleQuery {
            val category = dao.getCategory(categoryId)
            if (category != null) DataResult(category.toEntity()) else Failure(AppError(AppErrorType.NOT_FOUND))
        }

    override suspend fun updateCategory(category: Category) =
        RoomHandler.handleQuery {
            dao.updateCategory(category.toRoom())
            SuccessResult
        }

    override suspend fun deleteCategory(categoryId: String) =
        RoomHandler.handleQuery {
            dao.deleteCategory(categoryId)
            SuccessResult
        }

}
