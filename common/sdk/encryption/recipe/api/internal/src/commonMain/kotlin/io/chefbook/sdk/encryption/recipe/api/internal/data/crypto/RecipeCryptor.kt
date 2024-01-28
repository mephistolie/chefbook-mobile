package io.chefbook.sdk.encryption.recipe.api.internal.data.crypto

import io.chefbook.libs.encryption.SymmetricKey
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.crud.api.internal.data.models.DecryptedRecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.models.EncryptedRecipeInput

interface RecipeCryptor {

  fun encryptRecipeInfo(recipe: DecryptedRecipeInfo, key: SymmetricKey): EncryptedRecipeInfo

  fun decryptRecipeInfo(recipe: EncryptedRecipeInfo, key: SymmetricKey): DecryptedRecipeInfo

  fun decryptRecipe(recipe: EncryptedRecipe, key: SymmetricKey): DecryptedRecipe

  fun encryptRecipeInput(recipe: DecryptedRecipeInput, key: SymmetricKey): EncryptedRecipeInput
}
