package io.chefbook.sdk.category.impl.data.sources.remote.services

import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CategoryBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CreateCategoryRequestBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CreateCategoryResponseBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.UpdateCategoryRequestBody
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse

internal interface CategoryApiService {

  suspend fun getCategories(): Result<List<CategoryBody>>

  suspend fun createCategory(body: CreateCategoryRequestBody): Result<CreateCategoryResponseBody>

  suspend fun getCategory(categoryId: String): Result<CategoryBody>

  suspend fun updateCategory(
    categoryId: String,
    body: UpdateCategoryRequestBody
  ): Result<MessageResponse>

  suspend fun deleteCategory(categoryId: String): Result<MessageResponse>
}
