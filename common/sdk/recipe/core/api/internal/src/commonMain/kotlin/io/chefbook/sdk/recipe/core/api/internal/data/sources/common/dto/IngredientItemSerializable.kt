package io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto

import io.chefbook.common.models.measureunit.MeasureUnitMapper
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientItemSerializable(
  @SerialName("id")
  val id: String,
  @SerialName("text")
  val text: String,
  @SerialName("type")
  val type: String,
  @SerialName("amount")
  val amount: Float? = null,
  @SerialName("unit")
  val measureUnit: String? = null,
  @SerialName("recipeId")
  val recipeId: String? = null,
) {

  fun toEntity() = when (this.type.lowercase()) {
    TYPE_SECTION -> IngredientsItem.Section(
      id = id,
      name = text,
    )

    else -> IngredientsItem.Ingredient(
      id = id,
      name = text,
      amount = amount,
      measureUnit = MeasureUnitMapper.map(measureUnit),
      recipeId = recipeId,
    )
  }

  companion object {
    const val TYPE_INGREDIENT = "ingredient"
    const val TYPE_SECTION = "section"
    const val TYPE_ENCRYPTED_DATA = "encrypted_data"
  }
}

fun IngredientsItem.toSerializable() = when (this) {
  is IngredientsItem.Ingredient -> IngredientItemSerializable(
    id = id,
    text = name,
    type = IngredientItemSerializable.TYPE_INGREDIENT,
    amount = amount,
    measureUnit = MeasureUnitMapper.map(measureUnit),
    recipeId = recipeId,
  )

  is IngredientsItem.Section -> IngredientItemSerializable(
    id = id,
    text = name,
    type = IngredientItemSerializable.TYPE_SECTION,
  )
}
