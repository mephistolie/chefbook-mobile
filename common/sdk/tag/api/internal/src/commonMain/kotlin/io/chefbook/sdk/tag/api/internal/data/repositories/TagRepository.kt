package io.chefbook.sdk.tag.api.internal.data.repositories

import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {

  fun observeTags(): Flow<List<Tag>?>

  suspend fun getTags(): List<Tag>
}
