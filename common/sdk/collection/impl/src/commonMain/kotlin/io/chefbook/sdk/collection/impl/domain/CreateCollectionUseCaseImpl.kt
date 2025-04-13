package io.chefbook.sdk.collection.impl.domain

import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.collection.api.external.domain.usecases.CreateCollectionUseCase
import io.chefbook.sdk.collection.api.internal.data.repositories.CollectionRepository

internal class CreateCollectionUseCaseImpl(
  private val repo: CollectionRepository,
) : CreateCollectionUseCase {

  override suspend operator fun invoke(input: CollectionInput): Result<String> =
    repo.createCollection(input)
}
