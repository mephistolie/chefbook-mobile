package io.chefbook.sdk.tag.api.external.domain.usecases

import io.chefbook.sdk.tag.api.external.domain.entities.Tag
import kotlinx.coroutines.flow.Flow

interface ObserveTagsUseCase {
  operator fun invoke(): Flow<List<Tag>?>
}
