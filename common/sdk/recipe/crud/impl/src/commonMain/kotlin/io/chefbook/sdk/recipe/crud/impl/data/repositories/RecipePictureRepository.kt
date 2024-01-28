package io.chefbook.sdk.recipe.crud.impl.data.repositories

import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

internal interface RecipePictureRepository {
  suspend fun uploadRecipePictures(
    recipeId: String,
    pictures: RecipeInput.Pictures,
    key: SymmetricKey?,
  ): EmptyResult
}