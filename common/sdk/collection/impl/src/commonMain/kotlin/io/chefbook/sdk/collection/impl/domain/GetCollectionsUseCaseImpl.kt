package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.usecases.GetCollectionsUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository

internal class GetCollectionsUseCaseImpl(
  private val categoriesRepository: CollectionRepository,
) : GetCollectionsUseCase {

  override suspend operator fun invoke() = categoriesRepository.getCollections()
    .sortedBy { it.name }
}
