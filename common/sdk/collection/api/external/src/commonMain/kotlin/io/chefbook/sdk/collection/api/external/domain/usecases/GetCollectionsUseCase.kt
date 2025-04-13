package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.sdk.collection.api.external.domain.entities.Collection

interface GetCollectionsUseCase {
  suspend operator fun invoke(): List<Collection>
}
