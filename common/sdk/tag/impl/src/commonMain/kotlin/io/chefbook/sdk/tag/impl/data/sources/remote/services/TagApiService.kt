package io.chefbook.sdk.tag.impl.data.sources.remote.services

import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagSerializable
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable

internal interface TagApiService {

  suspend fun getTags(language: String? = null): Result<TagsSerializable>

  suspend fun getTag(tagId: String, language: String? = null): Result<TagSerializable>

  suspend fun getGroups(language: String? = null): Result<Map<String, String>>
}
