package io.chefbook.sdk.collection.api.external.domain.usecases

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import kotlinx.coroutines.flow.Flow

fun interface ObserveCollectionsUseCase {
  operator fun invoke(): Flow<List<Collection>?>
}
