package io.chefbook.sdk.tag.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlinx.coroutines.flow.Flow

internal interface LocalTagSource {

  fun observeTags(): Flow<List<Tag>>

  suspend fun getTags(): Result<List<Tag>>

  suspend fun cacheTags(tags: List<Tag>): EmptyResult
}
