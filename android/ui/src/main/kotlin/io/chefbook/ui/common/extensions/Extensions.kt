package io.chefbook.ui.common.extensions

import android.content.res.Resources
import io.chefbook.core.android.R
import io.chefbook.core.android.utils.minutesToTimeString
import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.measureunit.MeasureUnit

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
