package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.file.api.internal.data.repositories.FileRepository
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeInputPictureUseCase

internal class DeleteRecipeInputPictureUseCaseImpl(
  private val fileRepository: FileRepository,
) : DeleteRecipeInputPictureUseCase {

  override suspend operator fun invoke(path: String): EmptyResult =
    fileRepository.deleteFile(path)
}
