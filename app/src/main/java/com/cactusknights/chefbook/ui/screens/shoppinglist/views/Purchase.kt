package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import com.cactusknights.chefbook.ui.themes.ChefBookColors
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.themes.ChefBookTypography
import com.mephistolie.compost.ui.checkboxes.CircleCheckbox

@Composable
fun Purchase(
    purchase: Purchase,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        CircleCheckbox(
            isChecked = purchase.isPurchased,
            onClick = { },
            checkedColor = colors.tintPrimary,
            checkmarkColor = colors.tintSecondary,
            checkmarkSize = 20.dp,
            enabled = false,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = getFormattedText(purchase, colors, typography),
            style = typography.headline1,
            color = colors.foregroundPrimary,
        )
    }
}

@Composable
private fun getFormattedText(purchase: Purchase, colors: ChefBookColors, typography: ChefBookTypography) =
    buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = colors.foregroundPrimary,
                    fontSize = typography.headline1.fontSize,
                    fontWeight = typography.headline1.fontWeight,
                    fontStyle = typography.headline1.fontStyle,
                    fontFamily = typography.headline1.fontFamily,
                )
            ) {
                append(purchase.name)
            }
            if (purchase.multiplier > 1) {
                withStyle(
                    SpanStyle(
                        color = colors.foregroundSecondary,
                        fontSize = typography.headline2.fontSize,
                        fontWeight = typography.headline2.fontWeight,
                        fontStyle = typography.headline2.fontStyle,
                        fontFamily = ChefBookTheme.typography.headline2.fontFamily,
                    )
                ) {
                    append(" x${purchase.multiplier}")
                }
            }
        }