package io.chefbook.sdk.category.impl.data.sources.remote.services

import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CreateCategoryRequestBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CategoryBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CreateCategoryResponseBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.UpdateCategoryRequestBody
import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.network.api.internal.service.dto.responses.IdResponse
import io.chefbook.sdk.network.api.internal.service.dto.responses.MessageResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url

internal class CategoryApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), CategoryApiService {

  override suspend fun getCategories(): Result<List<CategoryBody>> = safeGet {
    url(CATEGORIES_ROUTE)
  }

  override suspend fun createCategory(category: CreateCategoryRequestBody): Result<CreateCategoryResponseBody> = safePost {
    url(CATEGORIES_ROUTE)
    setBody(body)
  }

  override suspend fun getCategory(categoryId: String): Result<CategoryBody> = safeGet {
    url("$CATEGORIES_ROUTE/$categoryId")
  }

  override suspend fun updateCategory(
    categoryId: String,
    body: UpdateCategoryRequestBody
  ): Result<MessageResponse> = safePut {
    url("$CATEGORIES_ROUTE/$categoryId")
    setBody(body)
  }

  override suspend fun deleteCategory(categoryId: String): Result<MessageResponse> = safeDelete {
    url("$CATEGORIES_ROUTE/$categoryId")
  }

  companion object {
    private const val CATEGORIES_ROUTE = "/v1/recipes/categories"
  }
}
