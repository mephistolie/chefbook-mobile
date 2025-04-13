package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.usecases.GetCollectionUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository

internal class GetCollectionUseCaseImpl(
  private val repo: CollectionRepository,
) : GetCollectionUseCase {

  override suspend operator fun invoke(collectionId: String) = repo.getCollection(collectionId)
}
