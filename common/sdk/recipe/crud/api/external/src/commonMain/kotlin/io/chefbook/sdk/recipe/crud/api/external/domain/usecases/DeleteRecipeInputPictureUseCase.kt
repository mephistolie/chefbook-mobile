package io.chefbook.sdk.recipe.crud.api.external.domain.usecases

import io.chefbook.libs.utils.result.EmptyResult

interface DeleteRecipeInputPictureUseCase {
  suspend operator fun invoke(path: String): EmptyResult
}
