package io.chefbook.sdk.tag.impl.data.sources.remote

import io.chefbook.sdk.tag.impl.data.sources.remote.services.TagApiService
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable

internal class RemoteTagSourceImpl(
  private val api: TagApiService,
) : RemoteTagSource {
  override suspend fun getTags(language: String?): Result<List<Tag>> =
    api.getTags(language).map(TagsSerializable::toEntity)
}
