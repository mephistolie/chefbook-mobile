package io.chefbook.sdk.encryption.recipe.impl.data.crypto

import io.chefbook.libs.crypto.encryption.HybridCryptor
import io.chefbook.libs.crypto.encryption.generateIV
import io.chefbook.libs.crypto.encryption.models.SymmetricCipherData
import io.chefbook.libs.crypto.encryption.models.SymmetricKey
import io.chefbook.sdk.encryption.recipe.api.internal.data.crypto.RecipeCryptor
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.EncryptedRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.IngredientItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.toSerializable
import io.chefbook.sdk.recipe.crud.api.internal.data.models.DecryptedRecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.models.EncryptedRecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.common.dto.toSerializable
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import kotlinx.serialization.json.Json

internal object RecipeCryptorImpl : RecipeCryptor {

  override fun encryptRecipeInfo(
    recipe: DecryptedRecipeInfo,
    key: SymmetricKey,
  ): EncryptedRecipeInfo = EncryptedRecipeInfo(
    meta = recipe.meta,
    isOwned = recipe.isOwned,
    isSaved = recipe.isSaved,
    collections = recipe.collections,
    isFavourite = recipe.isFavourite,
    servings = recipe.servings,
    time = recipe.time,
    calories = recipe.calories,
    name = encryptData(
      data = recipe.name.encodeToByteArray(),
      key = key,
      iv = generateRecipeNameIv(
        recipeId = recipe.id,
        version = recipe.version,
      )
    ),
    preview = recipe.preview,
  )

  override fun decryptRecipeInfo(
    recipe: EncryptedRecipeInfo,
    key: SymmetricKey,
  ) = DecryptedRecipeInfo(
    meta = recipe.meta,
    isOwned = recipe.isOwned,
    isSaved = recipe.isSaved,
    collections = recipe.collections,
    isFavourite = recipe.isFavourite,
    servings = recipe.servings,
    time = recipe.time,
    calories = recipe.calories,
    name = decryptData(
      data = recipe.name,
      key = key,
      iv = generateRecipeNameIv(
        recipeId = recipe.id,
        version = recipe.version,
      ),
    ),
    preview = recipe.preview,
  )

  override fun decryptRecipe(
    recipe: EncryptedRecipe,
    key: SymmetricKey,
  ) = DecryptedRecipe(
    info = decryptRecipeInfo(recipe.info, key),
    macronutrients = recipe.macronutrients,
    description = recipe.description?.let { description ->
      decryptData(
        data = description,
        key = key,
        iv = generateRecipeDescriptionIv(
          recipeId = recipe.id,
          version = recipe.version,
        ),
      )
    },
    ingredients = Json
      .decodeFromString<List<IngredientItemSerializable>>(
        string = decryptData(
          data = recipe.ingredients,
          key = key,
          iv = generateRecipeIngredientsIv(
            recipeId = recipe.id,
            version = recipe.version,
          ),
        )
      )
      .map(IngredientItemSerializable::toEntity),
    cooking = Json
      .decodeFromString<List<CookingItemSerializable>>(
        string = decryptData(
          data = recipe.cooking,
          key = key,
          iv = generateRecipeCookingIv(
            recipeId = recipe.id,
            version = recipe.version,
          ),
        ),
      ).map { it.toEntity((recipe.cookingPictures[it.id])) },
  )

  override fun encryptRecipeInput(
    recipe: DecryptedRecipeInput,
    key: SymmetricKey,
  ): EncryptedRecipeInput = EncryptedRecipeInput(
    id = recipe.id,
    visibility = recipe.visibility,
    language = recipe.language,

    servings = recipe.servings,
    time = recipe.time,

    calories = recipe.calories,
    macronutrients = recipe.macronutrients,

    originalVersion = recipe.targetVersion,

    name = encryptData(
      data = recipe.name.encodeToByteArray(),
      key = key,
      iv = generateRecipeNameIv(
        recipeId = recipe.id,
        version = recipe.targetVersion,
      )
    ),
    description = recipe.description?.let { description ->
      encryptData(
        data = description.encodeToByteArray(),
        key = key,
        iv = generateRecipeDescriptionIv(
          recipeId = recipe.id,
          version = recipe.targetVersion,
        ),
      )
    },
    pictures = recipe.pictures,
    ingredients = encryptData(
      data = Json
        .encodeToString(recipe.ingredients.map(Recipe.Decrypted.IngredientsItem::toSerializable))
        .encodeToByteArray(),
      key = key,
      iv = generateRecipeIngredientsIv(
        recipeId = recipe.id,
        version = recipe.targetVersion,
      ),
    ),
    cooking = encryptData(
      data = Json
        .encodeToString(recipe.cooking.map { it.toSerializable() })
        .encodeToByteArray(),
      key = key,
      iv = generateRecipeCookingIv(
        recipeId = recipe.id,
        version = recipe.targetVersion,
      ),
    ),
  )

  private fun generateRecipeNameIv(
    recipeId: String,
    version: Int,
  ): ByteArray = generateRecipeIv(
    recipeId = recipeId,
    version = version,
    property = "name",
  )

  private fun generateRecipeDescriptionIv(
    recipeId: String,
    version: Int,
  ): ByteArray = generateRecipeIv(
    recipeId = recipeId,
    version = version,
    property = "description",
  )

  private fun generateRecipeIngredientsIv(
    recipeId: String,
    version: Int,
  ): ByteArray = generateRecipeIv(
    recipeId = recipeId,
    version = version,
    property = "ingredients",
  )

  private fun generateRecipeCookingIv(
    recipeId: String,
    version: Int,
  ): ByteArray = generateRecipeIv(
    recipeId = recipeId,
    version = version,
    property = "cooking",
  )

  private fun generateRecipeIv(
    recipeId: String,
    version: Int,
    property: String,
  ): ByteArray = HybridCryptor.generateIV("$recipeId/$version/$property")

  private fun decryptData(
    data: String,
    key: SymmetricKey,
    iv: ByteArray,
  ): String =
    HybridCryptor.decryptBySymmetricKey(
      cipherData = SymmetricCipherData(
        combinedData = data.decodeBase64Bytes(),
        iv = iv,
      ),
      key = key,
    ).decodeToString()

  private fun encryptData(
    data: ByteArray,
    key: SymmetricKey,
    iv: ByteArray,
  ) =
    HybridCryptor.encryptBySymmetricKey(
      plaintext = data,
      key = key,
      iv = iv,
    ).combinedData.encodeBase64()
}
