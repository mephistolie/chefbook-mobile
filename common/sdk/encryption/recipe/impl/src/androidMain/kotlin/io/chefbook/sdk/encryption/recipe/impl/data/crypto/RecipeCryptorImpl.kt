package io.chefbook.sdk.encryption.recipe.impl.data.crypto

import io.chefbook.libs.encryption.HybridCryptor
import io.chefbook.libs.encryption.SymmetricKey
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object RecipeCryptorImpl : RecipeCryptor {

  override fun encryptRecipeInfo(recipe: DecryptedRecipeInfo, key: SymmetricKey) =
    EncryptedRecipeInfo(
      meta = recipe.meta,
      isOwned = recipe.isOwned,
      isSaved = recipe.isSaved,
      categories = recipe.categories,
      isFavourite = recipe.isFavourite,
      servings = recipe.servings,
      time = recipe.time,
      calories = recipe.calories,
      name = encryptData(recipe.name.toByteArray(), key),
      preview = recipe.preview,
    )

  override fun decryptRecipeInfo(recipe: EncryptedRecipeInfo, key: SymmetricKey) =
    DecryptedRecipeInfo(
      meta = recipe.meta,
      isOwned = recipe.isOwned,
      isSaved = recipe.isSaved,
      categories = recipe.categories,
      isFavourite = recipe.isFavourite,
      servings = recipe.servings,
      time = recipe.time,
      calories = recipe.calories,
      name = decryptData(recipe.name, key),
      preview = recipe.preview,
    )

  override fun decryptRecipe(recipe: EncryptedRecipe, key: SymmetricKey) = DecryptedRecipe(
    info = decryptRecipeInfo(recipe.info, key),
    macronutrients = recipe.macronutrients,
    description = recipe.description?.let { decryptData(it, key) },
    ingredients = Json
      .decodeFromString<List<IngredientItemSerializable>>(decryptData(recipe.ingredients, key))
      .map(IngredientItemSerializable::toEntity),
    cooking = Json
      .decodeFromString<List<CookingItemSerializable>>(decryptData(recipe.ingredients, key))
      .map { it.toEntity((recipe.cookingPictures[it.id])) },
  )

  override fun encryptRecipeInput(recipe: DecryptedRecipeInput, key: SymmetricKey) =
    EncryptedRecipeInput(
      id = recipe.id,
      visibility = recipe.visibility,
      language = recipe.language,

      servings = recipe.servings,
      time = recipe.time,

      calories = recipe.calories,
      macronutrients = recipe.macronutrients,


      version = recipe.version,

      name = encryptData(recipe.name.toByteArray(), key),
      description = recipe.description?.let { encryptData(it.toByteArray(), key) },
      pictures = recipe.pictures,
      ingredients = encryptData(
        data = Json
          .encodeToString(recipe.ingredients.map(Recipe.Decrypted.IngredientsItem::toSerializable))
          .toByteArray(),
        key = key
      ),
      cooking = encryptData(
        data = Json
          .encodeToString(recipe.cooking.map { it.toSerializable() })
          .toByteArray(),
        key = key
      ),
    )

  private fun decryptData(data: String, key: SymmetricKey) =
    String(HybridCryptor.decryptDataBySymmetricKey(data.decodeBase64Bytes(), key))

  private fun encryptData(data: ByteArray, key: SymmetricKey) =
    HybridCryptor.encryptDataBySymmetricKey(data, key).encodeBase64()
}
