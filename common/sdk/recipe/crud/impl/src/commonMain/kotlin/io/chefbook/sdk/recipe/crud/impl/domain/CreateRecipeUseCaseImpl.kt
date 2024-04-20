package io.chefbook.sdk.recipe.crud.impl.domain

import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.encryption.AsymmetricPublicKey
import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.vault.api.internal.data.repositories.EncryptedVaultRepository
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.CreateRecipeUseCase
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipePictureRepository
import kotlinx.coroutines.withContext

internal class CreateRecipeUseCaseImpl(
  private val recipeRepository: RecipeCrudRepository,
  private val pictureRepository: RecipePictureRepository,
  private val vaultRepository: EncryptedVaultRepository,
  private val encryptionRepository: RecipeEncryptionRepository,
  private val dispatchers: AppDispatchers,
) : CreateRecipeUseCase {

  override suspend operator fun invoke(input: RecipeInput): Result<String> =
    withContext(dispatchers.computation) {
      val recipeKey: SymmetricKey? =
        if (input.hasEncryption) encryptionRepository.generateRecipeKey() else null
      val vaultKey: AsymmetricPublicKey? = recipeKey?.let {
        vaultRepository.getVaultPublicKey()
          .onFailure { e -> return@withContext Result.failure(e) }
          .getOrNull()
      }

      val recipeId = recipeRepository.createRecipe(input, recipeKey)
        .onFailure { return@withContext Result.failure(it) }
        .getOrThrow()

      if (recipeKey != null && vaultKey != null) {
        encryptionRepository.setRecipeKey(recipeId, recipeKey, vaultKey).onFailure { e ->
          recipeRepository.deleteRecipe(recipeId)
          return@withContext Result.failure(e)
        }
      }

      pictureRepository.uploadRecipePictures(recipeId, input.pictures, recipeKey)

      return@withContext Result.success(recipeId)
    }
}
