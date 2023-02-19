package com.mysty.chefbook.api.category.data.local

import com.mysty.chefbook.api.category.data.local.dao.CategoryDao
import com.mysty.chefbook.api.category.data.local.tables.CategoryRoomEntity
import com.mysty.chefbook.api.category.data.local.tables.toEntity
import com.mysty.chefbook.api.category.data.local.tables.toRoom
import com.mysty.chefbook.api.category.data.repository.ILocalCategorySource
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.Failure
import com.mysty.chefbook.api.common.communication.SuccessResult
import com.mysty.chefbook.api.common.communication.errors.AppError
import com.mysty.chefbook.api.common.communication.errors.AppErrorType
import com.mysty.chefbook.api.common.room.RoomHandler

internal class LocalCategorySource(
    private val dao: CategoryDao,
) : ILocalCategorySource {

    override suspend fun getCategories(): ActionStatus<List<Category>> =
        RoomHandler.handleQuery { DataResult(dao.getCategories().map(CategoryRoomEntity::toEntity)) }


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
