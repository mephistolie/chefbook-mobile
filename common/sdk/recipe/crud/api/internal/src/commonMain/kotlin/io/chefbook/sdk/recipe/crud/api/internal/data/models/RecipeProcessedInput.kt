package io.chefbook.sdk.recipe.crud.api.internal.data.models

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

typealias DecryptedRecipeInput = RecipeProcessedInput.Decrypted
typealias EncryptedRecipeInput = RecipeProcessedInput.Encrypted

sealed class RecipeProcessedInput(
  open val id: String,

  open val visibility: RecipeMeta.Visibility,
  open val isEncrypted: Boolean,
  open val language: Language,

  open val servings: Int?,
  open val time: Int?,

  open val calories: Int?,
  open val macronutrients: Recipe.Macronutrients?,

  open val version: Int,
) {

  data class Decrypted(
    override val id: String,

    override val visibility: RecipeMeta.Visibility,
    override val isEncrypted: Boolean,
    override val language: Language,

    override val servings: Int?,
    override val time: Int?,

    override val calories: Int?,
    override val macronutrients: Recipe.Macronutrients?,

    override val version: Int,

    val name: String,
    val description: String?,
    val preview: RecipeInput.Picture?,
    val ingredients: List<Recipe.Decrypted.IngredientsItem>,
    val cooking: List<RecipeInput.CookingItem>,
  ) : RecipeProcessedInput(
    id = id,

    visibility = visibility,
    isEncrypted = isEncrypted,
    language = language,

    servings = servings,
    time = time,

    calories = calories,
    macronutrients = macronutrients,

    version = version,
  ) {
    val pictures
      get() = RecipeInput.Pictures(
        preview = preview,
        cooking = cooking.filterIsInstance<RecipeInput.CookingItem.Step>()
          .filter { it.pictures.isNotEmpty() }.associate { it.id to it.pictures },
      )
  }

  data class Encrypted(
    override val id: String,

    override val visibility: RecipeMeta.Visibility,
    override val language: Language,

    override val servings: Int?,
    override val time: Int?,

    override val calories: Int?,
    override val macronutrients: Recipe.Macronutrients?,

    override val version: Int,

    val name: String,
    val description: String?,
    val pictures: RecipeInput.Pictures,
    val ingredients: String,
    val cooking: String,
  ) : RecipeProcessedInput(
    id = id,

    visibility = visibility,
    isEncrypted = true,
    language = language,

    servings = servings,
    time = time,

    calories = calories,
    macronutrients = macronutrients,

    version = version,
  )
}