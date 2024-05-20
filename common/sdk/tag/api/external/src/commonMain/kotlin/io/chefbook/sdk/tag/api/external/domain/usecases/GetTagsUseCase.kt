package io.chefbook.sdk.tag.api.external.domain.usecases

import io.chefbook.sdk.tag.api.external.domain.entities.Tag

interface GetTagsUseCase {
  suspend operator fun invoke(): List<Tag>
}
