package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput.CookingItem
import io.chefbook.sdk.recipe.core.api.internal.data.sources.remote.services.dto.VisibilitySerializable
import io.chefbook.sdk.recipe.crud.api.internal.data.models.RecipeProcessedInput
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.IngredientItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.toSerializable
import io.chefbook.sdk.recipe.crud.api.internal.data.models.DecryptedRecipeInput
import io.chefbook.sdk.recipe.crud.api.internal.data.models.EncryptedRecipeInput
import io.chefbook.sdk.recipe.crud.impl.data.sources.common.dto.toSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecipeInputRequest(
  @SerialName("recipeId")
  val id: String?,
  @SerialName("name")
  val name: String,

  @SerialName("visibility")
  val visibility: VisibilitySerializable,
  @SerialName("encrypted")
  val isEncrypted: Boolean,

  @SerialName("language")
  val language: String,
  @SerialName("description")
  val description: String?,

  @SerialName("version")
  val version: Int?,

  @SerialName("servings")
  val servings: Int?,
  @SerialName("time")
  val time: Int?,

  @SerialName("calories")
  val calories: Int?,
  @SerialName("macronutrients")
  val macronutrients: MacronutrientsBody?,

  @SerialName("ingredients")
  val ingredients: List<IngredientItemSerializable>,
  @SerialName("cooking")
  val cooking: List<CookingItemSerializable>,
)

internal fun RecipeProcessedInput.toSerializable() =
  when (this) {
    is DecryptedRecipeInput -> RecipeInputRequest(
      id = id,
      name = name,
      visibility = when (visibility) {
        RecipeMeta.Visibility.PRIVATE -> VisibilitySerializable.PRIVATE
        RecipeMeta.Visibility.LINK -> VisibilitySerializable.LINK
        RecipeMeta.Visibility.PUBLIC -> VisibilitySerializable.PUBLIC
      },
      isEncrypted = isEncrypted,
      language = language.code,
      description = description,

      servings = servings,
      time = time,

      calories = calories,
      macronutrients = macronutrients?.toSerializable(),

      ingredients = ingredients.map(IngredientsItem::toSerializable),
      cooking = cooking.map(CookingItem::toSerializable),

      version = version,
    )

    is EncryptedRecipeInput -> RecipeInputRequest(
      id = id,
      name = name,
      visibility = when (visibility) {
        RecipeMeta.Visibility.PRIVATE -> VisibilitySerializable.PRIVATE
        RecipeMeta.Visibility.LINK -> VisibilitySerializable.LINK
        RecipeMeta.Visibility.PUBLIC -> VisibilitySerializable.PUBLIC
      },
      isEncrypted = isEncrypted,
      language = language.code,
      description = description,

      servings = servings,
      time = time,

      calories = calories,
      macronutrients = macronutrients?.toSerializable(),

      ingredients = listOf(
        IngredientItemSerializable(
          id = generateUUID(),
          text = ingredients,
          type = IngredientItemSerializable.TYPE_ENCRYPTED_DATA,
        )
      ),
      cooking = listOf(
        CookingItemSerializable(
          id = generateUUID(),
          text = cooking,
          type = CookingItemSerializable.TYPE_ENCRYPTED_DATA,
        )
      ),

      version = version,
    )
  }
