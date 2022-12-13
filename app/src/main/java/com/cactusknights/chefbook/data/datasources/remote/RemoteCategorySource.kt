package com.cactusknights.chefbook.data.datasources.remote

import com.cactusknights.chefbook.data.IRemoteCategorySource
import com.cactusknights.chefbook.data.dto.remote.categories.toEntity
import com.cactusknights.chefbook.data.dto.remote.categories.toRequest
import com.cactusknights.chefbook.data.dto.remote.common.body
import com.cactusknights.chefbook.data.dto.remote.common.isFailure
import com.cactusknights.chefbook.data.dto.remote.common.toActionStatus
import com.cactusknights.chefbook.data.network.INetworkHandler
import com.cactusknights.chefbook.data.network.api.CategoryApi
import com.cactusknights.chefbook.domain.entities.action.ActionStatus
import com.cactusknights.chefbook.domain.entities.action.DataResult
import com.cactusknights.chefbook.domain.entities.action.asEmpty
import com.cactusknights.chefbook.domain.entities.action.asFailure
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import java.util.*

class RemoteCategorySource(
    private val api: CategoryApi,
    private val handleResponse: INetworkHandler,
) : IRemoteCategorySource {
    override suspend fun getCategories(): ActionStatus<List<Category>> {
        val result = handleResponse { api.getCategories() }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().map { it.toEntity() })
    }

    override suspend fun createCategory(input: CategoryInput): ActionStatus<String> {
        val result = handleResponse { api.addCategory(input.toRequest(categoryId = UUID.randomUUID().toString())) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().id)
    }

    override suspend fun getCategory(categoryId: String): ActionStatus<Category> {
        val result = handleResponse { api.getCategory(categoryId) }
        if (result.isFailure()) return result.toActionStatus().asFailure()

        return DataResult(result.body().toEntity())
    }

    override suspend fun updateCategory(categoryId: String, input: CategoryInput) =
        handleResponse { api.updateCategory(categoryId, input.toRequest()) }.toActionStatus().asEmpty()

    override suspend fun deleteCategory(categoryId: String) =
        handleResponse { api.deleteCategory(categoryId) }.toActionStatus().asEmpty()
}
