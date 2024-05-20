package io.chefbook.sdk.tag.impl.domain

import io.chefbook.sdk.tag.api.external.domain.usecases.ObserveTagsUseCase
import io.chefbook.sdk.tag.api.internal.data.repositories.TagRepository

internal class ObserveTagsUseCaseImpl(
  private val repository: TagRepository,
) : ObserveTagsUseCase {

  override operator fun invoke() = repository.observeTags()
}
