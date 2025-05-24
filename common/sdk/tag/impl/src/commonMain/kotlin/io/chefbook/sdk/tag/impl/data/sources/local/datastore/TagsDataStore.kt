package io.chefbook.sdk.tag.impl.data.sources.local.datastore

import androidx.datastore.core.DataStore
import io.chefbook.sdk.database.api.internal.ChefBookDataStoreFactory
import io.chefbook.sdk.tag.api.internal.data.sources.common.dto.TagsSerializable

internal interface TagsDataStore : DataStore<TagsSerializable>

internal class TagsDataStoreImpl(
  factory: ChefBookDataStoreFactory,
) : TagsDataStore, DataStore<TagsSerializable> by factory.create(
  fileName = "tags.json",
  serializer = TagsSerializer,
)
