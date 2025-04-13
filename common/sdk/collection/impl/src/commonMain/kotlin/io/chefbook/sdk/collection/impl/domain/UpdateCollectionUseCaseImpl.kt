package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.api.external.domain.usecases.UpdateCollectionUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository

internal class UpdateCollectionUseCaseImpl(
  private val repo: CollectionRepository,
) : UpdateCollectionUseCase {

  override suspend operator fun invoke(collectionId: String, input: CollectionInput) =
    repo.updateCollection(collectionId, input)
}
