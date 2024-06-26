package io.chefbook.features.recipe.info.ui.components.ingredients

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.checkboxes.Checkbox
import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.libs.utils.numbers.toFormattedInput
import io.chefbook.libs.utils.numbers.toFormattedText
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.ui.common.extensions.localizedName
import kotlin.math.abs

@Composable
internal fun Ingredient(
  ingredient: IngredientsItem.Ingredient,
  amountRatio: Float,
  isChecked: Boolean,
  modifier: Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start,
    modifier = modifier.fillMaxSize(),
  ) {
    Checkbox(
      isChecked = isChecked,
      onClick = {},
      checkmarkSize = 20.dp,
      isEnabled = false,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = buildIngredientName(ingredient = ingredient, amountRatio = amountRatio),
      style = typography.headline1,
      color = colors.foregroundPrimary,
    )
  }
}

@Composable
private fun buildIngredientName(
  ingredient: IngredientsItem.Ingredient,
  amountRatio: Float,
): AnnotatedString {
  val resources = LocalContext.current.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  return buildAnnotatedString {
    val requiredTextStyle = SpanStyle(
      color = colors.foregroundPrimary,
      fontSize = typography.headline1.fontSize,
      fontWeight = typography.headline1.fontWeight,
      fontStyle = typography.headline1.fontStyle,
      fontFamily = typography.headline1.fontFamily,
    )
    val optionalTextStyle = SpanStyle(
      color = colors.foregroundSecondary,
      fontSize = typography.headline2.fontSize,
      fontWeight = typography.headline2.fontWeight,
      fontStyle = typography.headline2.fontStyle,
      fontFamily = typography.headline2.fontFamily,
    )

    withStyle(requiredTextStyle) { append(ingredient.name) }
    append(" ")
    ingredient.amount?.let { amount ->
      withStyle(optionalTextStyle) {
        append((amount * amountRatio).toFormattedText())
        append(" ")
      }
    }
    ingredient.measureUnit?.let { unit ->
      if (unit !is MeasureUnit.Custom || unit.name.isNotEmpty()) {
        withStyle(optionalTextStyle) { append(unit.localizedName(resources)) }
      }
    }
  }
}