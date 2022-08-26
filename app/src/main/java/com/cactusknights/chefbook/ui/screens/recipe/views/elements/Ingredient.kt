package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.core.ui.localizedName
import com.cactusknights.chefbook.domain.entities.common.MeasureUnit
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.checkboxes.CircleCheckbox
import kotlin.math.abs

@Composable
fun Ingredient(
    ingredient: IngredientItem.Ingredient,
    amountRatio: Float,
    isChecked: Boolean,
    modifier: Modifier,
) {
    val resources = LocalContext.current.resources

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val text = buildAnnotatedString {
        withStyle(SpanStyle(
            color = colors.foregroundPrimary,
            fontSize = typography.headline1.fontSize,
            fontWeight = typography.headline1.fontWeight,
            fontStyle = typography.headline1.fontStyle,
            fontFamily = typography.headline1.fontFamily,
        )) {
            append(ingredient.name)
        }
        append(" ")
        ingredient.amount?.let { amount ->
            withStyle(SpanStyle(
                color = colors.foregroundSecondary,
                fontSize = typography.headline2.fontSize,
                fontWeight = typography.headline2.fontWeight,
                fontStyle = typography.headline2.fontStyle,
                fontFamily = ChefBookTheme.typography.headline2.fontFamily,
            )) {
                val amountForServings = amount * amountRatio
                val epsilon = 0.1F
                if (amountForServings > 10 || abs(amountForServings - amountForServings.toInt()) < epsilon) {
                    append("${amountForServings.toInt()}")
                } else {
                    append(String.format("%.2f", amountForServings))
                }
                append(" ")
            }
        }
        if (ingredient.unit != null && (ingredient.unit !is MeasureUnit.Custom || ingredient.unit.name.isNotEmpty())) {
            withStyle(SpanStyle(
                color = colors.foregroundSecondary,
                fontSize = typography.headline2.fontSize,
                fontWeight = typography.headline2.fontWeight,
                fontStyle = typography.headline2.fontStyle,
                fontFamily = typography.headline2.fontFamily,
            )) {
                append(ingredient.unit.localizedName(resources))
            }
        }
    }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxSize(),
    ) {
        CircleCheckbox(
            isChecked = isChecked,
            onCheckedChange = { },
            iconPadding = 2.dp,
            enabled = false,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = typography.headline1,
            color = colors.foregroundPrimary,
        )
    }
}
