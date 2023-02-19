package com.mysty.chefbook.features.recipe.input.ui.screens.ingredients.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape8
import com.mysty.chefbook.features.recipe.input.R
import com.mysty.chefbook.ui.common.extensions.localizedName

@Composable
internal fun IngredientField(
    ingredient: IngredientItem.Ingredient,
    onInputClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resources = LocalContext.current.resources

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val secondarySpan = SpanStyle(
        fontSize = typography.headline2.fontSize,
        fontWeight = typography.headline2.fontWeight,
        fontStyle = typography.headline2.fontStyle,
        fontFamily = typography.headline2.fontFamily,
        color = colors.foregroundSecondary,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp)
            .clippedBackground(colors.backgroundPrimary, RoundedCornerShape8)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drag_indicator),
            contentDescription = null,
            tint = colors.foregroundSecondary,
            modifier = Modifier
                .padding(top = 2.dp)
                .height(18.dp)
                .wrapContentWidth()
        )
        ThemedIndicatorTextField(
            value = ingredient.name,
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth()
                .simpleClickable(onClick = onInputClick),
            onValueChange = {},
            visualTransformation = { name ->
                var text = name
                ingredient.amount?.let { amount ->
                    text += AnnotatedString(
                        text = " $amount",
                        spanStyle = secondarySpan,
                    )
                }
                ingredient.unit?.let { unit ->
                    text += AnnotatedString(
                        text = " ${unit.localizedName(resources)}",
                        spanStyle = secondarySpan,
                    )
                }
                TransformedText(text, offsetMapping = OffsetMapping.Identity)
            },
            enabled = false,
            readOnly = false,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            label = {
                Text(
                    text = stringResource(R.string.common_general_ingredient),
                    color = colors.foregroundPrimary
                )
            },
            maxLines = 1,
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_cross),
            contentDescription = null,
            tint = colors.foregroundPrimary,
            modifier = Modifier
                .size(24.dp)
                .simpleClickable(onClick = onDeleteClick)
                .padding(2.dp)
        )
    }
}