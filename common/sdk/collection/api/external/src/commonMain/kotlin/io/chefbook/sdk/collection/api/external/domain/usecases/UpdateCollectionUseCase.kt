package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput

interface UpdateCollectionUseCase {
  suspend operator fun invoke(collectionId: String, input: CollectionInput): Result<Collection>
}
