package io.chefbook.sdk.tag.impl.domain

import io.chefbook.sdk.tag.api.external.domain.usecases.GetTagsUseCase
import io.chefbook.sdk.tag.api.internal.data.repositories.TagRepository

internal class GetTagsUseCaseImpl(
  private val repository: TagRepository,
) : GetTagsUseCase {

  override suspend operator fun invoke() = repository.getTags()
}
