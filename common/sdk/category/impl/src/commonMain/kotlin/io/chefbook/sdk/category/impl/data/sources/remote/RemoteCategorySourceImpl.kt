package io.chefbook.sdk.category.impl.data.sources.remote

import io.chefbook.libs.utils.result.asEmpty
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.category.impl.data.sources.remote.services.CategoryApiService
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CategoryBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.CreateCategoryResponseBody
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.toCreateCategoryRequest
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.toEntity
import io.chefbook.sdk.category.impl.data.sources.remote.services.dto.toUpdateCategoryRequest

internal class RemoteCategorySourceImpl(
  private val api: CategoryApiService,
) : RemoteCategorySource {

  override suspend fun getCategories() =
    api.getCategories().map { categories -> categories.map(CategoryBody::toEntity) }

  override suspend fun createCategory(input: CategoryInput) =
    api.createCategory(input.toCreateCategoryRequest(categoryId = generateUUID()))
      .map(CreateCategoryResponseBody::categoryId)

  override suspend fun getCategory(categoryId: String) =
    api.getCategory(categoryId).map(CategoryBody::toEntity)

  override suspend fun updateCategory(categoryId: String, input: CategoryInput) =
    api.updateCategory(categoryId, input.toUpdateCategoryRequest()).asEmpty()

  override suspend fun deleteCategory(categoryId: String) =
    api.deleteCategory(categoryId).asEmpty()
}
