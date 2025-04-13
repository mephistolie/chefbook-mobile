package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.sdk.collection.api.external.domain.entities.Collection

interface GetCollectionUseCase {
  suspend operator fun invoke(collectionId: String): Result<Collection>
}
