package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.usecases.ObserveCollectionsUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository
import kotlinx.coroutines.flow.onStart

internal class ObserveCollectionsUseCaseImpl(
  private val repo: CollectionRepository,
) : ObserveCollectionsUseCase {

  override operator fun invoke() = repo.observeCollections()
    .onStart { emit(null) }
}
