package io.chefbook.sdk.collection.impl.data.sources.local

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.impl.data.sources.CollectionSource

internal interface LocalCollectionSource : CollectionSource {

  suspend fun insertCollection(collection: Collection): Result<String>

  suspend fun clearUnusedCollections(): EmptyResult
}
