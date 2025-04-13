package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteCollectionUseCase {
  suspend operator fun invoke(collectionId: String): EmptyResult
}