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

fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    animated: Boolean = false,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier,
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    val alpha = remember { Animatable(if (animated) 0F else 1F) }
                    val scale = remember { Animatable(if (animated) 0.9F else 1F) }

                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .alpha(alpha.value)
                            .scale(scale.value),
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex])
                    }

                    if (animated) {
                        LaunchedEffect(Unit) {
                            launch { alpha.animateTo(1F, tween(500)) }
                            launch { scale.animateTo(1F, tween(500)) }
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
    }
}

fun LazyListScope.gridItems(
    size: Int,
    columnCount: Int,
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    animated: Boolean = false,
    itemContent: @Composable BoxScope.(Int) -> Unit,
) {
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    val alpha = remember { Animatable(if (animated) 0F else 1F) }
                    val scale = remember { Animatable(if (animated) 0.9F else 1F) }

                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .alpha(alpha.value)
                            .scale(scale.value),
                        propagateMinConstraints = true
                    ) {
                        itemContent(itemIndex)
                    }

                    LaunchedEffect(Unit) {
                        if (animated) {
                            launch { alpha.animateTo(1F, tween(500)) }
                            launch { scale.animateTo(1F, tween(500)) }
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1F))
                }

            }
        }
    }
}

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
