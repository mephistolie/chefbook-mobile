package io.chefbook.sdk.tag.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.toSerializable
import io.chefbook.sdk.tag.impl.data.sources.local.datastore.TagsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LocalTagSourceImpl(
  private val context: Context,
) : LocalTagSource {

  private val dataStore = DataStoreFactory.create(
    serializer = TagsSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeTags() =
    dataStore.data.map(TagsSerializable::toEntity)

  override suspend fun getTags(): Result<List<Tag>> =
    Result.success(dataStore.data.first().toEntity())

  override suspend fun cacheTags(tags: List<Tag>): EmptyResult {
    dataStore.updateData { tags.toSerializable() }
    return successResult
  }

  companion object {
    private const val DATASTORE_FILE = "tags.json"
  }
}
