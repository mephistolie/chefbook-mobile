package com.cactusknights.chefbook.core.ui

import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.MeasureUnit
import kotlinx.coroutines.launch

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
