package io.chefbook.sdk.collection.impl.data.sources.remote

import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.impl.data.sources.CollectionSource

internal interface RemoteCollectionSource : CollectionSource {

  suspend fun createCollection(input: CollectionInput): Result<String>
}
