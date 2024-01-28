package io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.dto.crud

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Macronutrients
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MacronutrientsBody(
  @SerialName("protein")
  val protein: Int? = null,
  @SerialName("fats")
  val fats: Int? = null,
  @SerialName("carbohydrates")
  val carbohydrates: Int? = null,
) {
  fun toEntity(): Macronutrients =
    Macronutrients(
      protein = protein,
      fats = fats,
      carbohydrates = carbohydrates,
    )
}

internal fun Macronutrients.toSerializable(): MacronutrientsBody =
  MacronutrientsBody(
    protein = protein,
    fats = fats,
    carbohydrates = carbohydrates,
  )
