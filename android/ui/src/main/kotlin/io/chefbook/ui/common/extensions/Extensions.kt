package io.chefbook.ui.common.extensions

import android.content.res.Resources
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.core.android.R
import io.chefbook.core.android.utils.minutesToTimeString

fun MeasureUnit.localizedName(resources: Resources) =
  when (this) {
    is MeasureUnit.G -> resources.getString(R.string.common_general_g)
    is MeasureUnit.KG -> resources.getString(R.string.common_general_kg)
    is MeasureUnit.ML -> resources.getString(R.string.common_general_ml)
    is MeasureUnit.L -> resources.getString(R.string.common_general_l)
    is MeasureUnit.PCS -> resources.getString(R.string.common_general_pcs)
    is MeasureUnit.TSP -> resources.getString(R.string.common_general_tsp)
    is MeasureUnit.TBSP -> resources.getString(R.string.common_general_tbsp)
    is MeasureUnit.Custom -> name
  }

fun Language.localizedName(resources: Resources) =
  when (this) {
    Language.ENGLISH -> resources.getString(R.string.common_general_language_english)
    Language.RUSSIAN -> resources.getString(R.string.common_general_language_russian)
    Language.UKRAINIAN -> resources.getString(R.string.common_general_language_ukrainian)
    Language.BELARUSIAN -> resources.getString(R.string.common_general_language_belarusian)
    Language.KAZAKH -> resources.getString(R.string.common_general_language_kazakh)
    Language.ARMENIAN -> resources.getString(R.string.common_general_language_armenian)
    Language.GEORGIAN -> resources.getString(R.string.common_general_language_georgian)
    Language.SERBIAN -> resources.getString(R.string.common_general_language_serbian)
    Language.FRENCH -> resources.getString(R.string.common_general_language_french)
    Language.GERMAN -> resources.getString(R.string.common_general_language_german)
    Language.ITALIAN -> resources.getString(R.string.common_general_language_italian)
    Language.SPANISH -> resources.getString(R.string.common_general_language_spanish)
    Language.TURKISH -> resources.getString(R.string.common_general_language_turkish)
    Language.HINDI -> resources.getString(R.string.common_general_language_hindi)
    Language.PORTUGUESE -> resources.getString(R.string.common_general_language_portuguese)
    Language.BENGALI -> resources.getString(R.string.common_general_language_bengali)
    Language.JAPANESE -> resources.getString(R.string.common_general_language_japanese)
    Language.KOREAN -> resources.getString(R.string.common_general_language_korean)
    Language.CHINESE -> resources.getString(R.string.common_general_language_chinese)
    Language.ARABIAN -> resources.getString(R.string.common_general_language_arabian)
    Language.PERSIAN -> resources.getString(R.string.common_general_language_persian)
    Language.OTHER -> resources.getString(R.string.common_general_language_other)
  }

fun stringToMeasureUnit(unit: String?, resources: Resources): MeasureUnit? {
  if (unit.isNullOrBlank()) return null
  val localizedUnitsMap = mapOf(
    resources.getString(R.string.common_general_g).lowercase() to MeasureUnit.G,
    resources.getString(R.string.common_general_kg).lowercase() to MeasureUnit.KG,
    resources.getString(R.string.common_general_ml).lowercase() to MeasureUnit.ML,
    resources.getString(R.string.common_general_l).lowercase() to MeasureUnit.L,
    resources.getString(R.string.common_general_pcs).lowercase() to MeasureUnit.PCS,
    resources.getString(R.string.common_general_tsp).lowercase() to MeasureUnit.TSP,
    resources.getString(R.string.common_general_tbsp).lowercase() to MeasureUnit.TBSP,
  )
  return localizedUnitsMap[unit.lowercase()] ?: MeasureUnit.Custom(unit)
}


fun DecryptedRecipe.asText(resources: Resources): String {
  var text = name.uppercase()

  owner.name?.let { author ->
    text += "\n\n${resources.getString(R.string.common_general_author)}: $author"
  }

  description?.let { description ->
    text += "\n\n${resources.getString(R.string.common_general_description)}:\n$description"
  }

  text += "\n"
  servings?.let {
    text += "\n${resources.getString(R.string.common_general_servings)}: $servings"
  }
  time?.let { time ->
    text += "\n${resources.getString(R.string.common_general_time)}: ${
      minutesToTimeString(
        time,
        resources
      )
    }"
  }

  if (hasDietData) {
    text += "\n\n${resources.getString(R.string.common_general_in_100_g)}:\n"
    calories?.let { calories ->
      text += "${resources.getString(R.string.common_general_calories)}: $calories ${
        resources.getString(
          R.string.common_general_kcal
        )
      }\n"
    }
    macronutrients?.protein?.let { protein ->
      text += "${resources.getString(R.string.common_general_protein)}: $protein\n"
    }
    macronutrients?.fats?.let { fats ->
      text += "${resources.getString(R.string.common_general_fats)}: $fats\n"
    }
    macronutrients?.carbohydrates?.let { carbohydrates ->
      text += "${resources.getString(R.string.common_general_carbs)}: $carbohydrates"
    }
  }

  text += "\n\n${resources.getString(R.string.common_general_ingredients).uppercase()}\n"
  for (ingredient in ingredients) {
    when (ingredient) {
      is IngredientsItem.Section -> {
        text += "${ingredient.name}:\n"
      }
      is IngredientsItem.Ingredient -> {
        text += "• ${ingredient.name}"
        ingredient.amount?.let { amount ->
          text += " - $amount"
          ingredient.measureUnit?.let { unit ->
            text += " ${unit.localizedName(resources)}"
          }
        }
        text += "\n"
      }
      else -> Unit
    }
  }
  text += "\n\n${resources.getString(R.string.common_general_cooking).uppercase()}\n"
  var stepCount = 1
  for (cookingItem in cooking) {
    when (cookingItem) {
      is CookingItem.Section -> {
        text += "${cookingItem.name}:\n"
      }
      is CookingItem.Step -> {
        text += "$stepCount. ${cookingItem.description}\n"
        stepCount++
      }
      else -> Unit
    }
  }

  text += "\n${resources.getString(R.string.common_general_recipe)} #${id}, ${
    resources.getString(
      R.string.app_name
    )
  }"

  return text
}
