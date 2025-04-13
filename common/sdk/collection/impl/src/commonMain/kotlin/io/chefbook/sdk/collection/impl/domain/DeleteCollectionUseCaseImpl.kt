package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.usecases.DeleteCollectionUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository

internal class DeleteCollectionUseCaseImpl(
  private val repo: CollectionRepository,
) : DeleteCollectionUseCase {

  override suspend operator fun invoke(collectionId: String) = repo.deleteCollection(collectionId)
}
