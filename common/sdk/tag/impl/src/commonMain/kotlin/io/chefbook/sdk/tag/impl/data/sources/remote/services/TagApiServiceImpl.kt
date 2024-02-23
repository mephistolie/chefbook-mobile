package io.chefbook.sdk.tag.impl.data.sources.remote.services

import io.chefbook.sdk.network.api.internal.service.ChefBookApiService
import io.chefbook.sdk.tag.impl.data.sources.common.dto.TagSerializable
import io.chefbook.sdk.tag.impl.data.sources.common.dto.TagsSerializable
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

internal class TagApiServiceImpl(
  client: HttpClient,
) : ChefBookApiService(client), TagApiService {

  override suspend fun getTags(language: String?): Result<TagsSerializable> = safeGet(TAGS_ROUTE) {
    parameter(LANGUAGE_PARAM, language)
  }

  override suspend fun getTag(tagId: String, language: String?): Result<TagSerializable> =
    safeGet("$TAGS_ROUTE/$tagId") {
      parameter(LANGUAGE_PARAM, language)
    }

  override suspend fun getGroups(language: String?): Result<Map<String, String>> =
    safeGet("$TAGS_ROUTE/groups") {
      parameter(LANGUAGE_PARAM, language)
    }

  companion object {
    private const val TAGS_ROUTE = "/v1/recipes/tags"

    private const val LANGUAGE_PARAM = "language"
  }
}
