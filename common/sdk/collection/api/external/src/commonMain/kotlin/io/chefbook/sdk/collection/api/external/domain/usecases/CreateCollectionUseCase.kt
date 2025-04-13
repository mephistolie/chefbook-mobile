package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput

interface CreateCollectionUseCase {
  suspend operator fun invoke(input: CollectionInput): Result<String>
}
