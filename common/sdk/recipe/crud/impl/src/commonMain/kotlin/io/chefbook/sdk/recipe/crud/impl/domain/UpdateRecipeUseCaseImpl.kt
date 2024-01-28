package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.libs.exceptions.notFoundResult
import io.chefbook.libs.utils.result.EmptyResult
import io.chefbook.libs.utils.result.successResult
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.UpdateRecipeUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipePictureRepository
import kotlinx.coroutines.withContext

internal class UpdateRecipeUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
  private val pictureRepository: RecipePictureRepository,
  private val vaultRepository: EncryptedVaultRepository,
  private val encryptionRepository: RecipeEncryptionRepository,
  private val dispatchers: AppDispatchers,
) : UpdateRecipeUseCase {

  override suspend operator fun invoke(input: RecipeInput): EmptyResult = withContext(dispatchers.computation) {
    val recipeKey: SymmetricKey? = if (input.hasEncryption) {
      getRecipeKey(input.id)
        .onFailure { e -> return@withContext Result.failure(e) }
        .getOrNull()
    } else {
      null
    }

    val updateRecipeResult = recipeRepository.updateRecipe(input, recipeKey)
    if (updateRecipeResult.isFailure) return@withContext Result.failure(updateRecipeResult.exceptionOrNull()!!)

    pictureRepository.uploadRecipePictures(input.id, input.pictures, recipeKey)

    return@withContext successResult
  }

  private suspend inline fun getRecipeKey(recipeId: String): Result<SymmetricKey> =
    vaultRepository.getVaultPrivateKey()
      .onFailure { e -> return Result.failure(e) }
      .map { vaultKey ->
        encryptionRepository.getRecipeKey(recipeId, vaultKey).getOrNull() ?: return notFoundResult()
      }
}
