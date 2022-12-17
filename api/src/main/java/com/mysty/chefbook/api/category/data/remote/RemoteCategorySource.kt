package com.mysty.chefbook.api.category.data.remote

import com.mysty.chefbook.api.category.data.remote.api.CategoryApi
import com.mysty.chefbook.api.category.data.remote.dto.toEntity
import com.mysty.chefbook.api.category.data.remote.dto.toRequest
import com.mysty.chefbook.api.category.data.repository.IRemoteCategorySource
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.api.common.communication.ActionStatus
import com.mysty.chefbook.api.common.communication.DataResult
import com.mysty.chefbook.api.common.communication.asEmpty
import com.mysty.chefbook.api.common.communication.asFailure
import com.mysty.chefbook.api.common.network.INetworkHandler
import com.mysty.chefbook.api.common.network.dto.body
import com.mysty.chefbook.api.common.network.dto.isFailure
import com.mysty.chefbook.api.common.network.dto.toActionStatus
import java.util.UUID

internal class RemoteCategorySource(
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
