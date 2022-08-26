package com.cactusknights.chefbook.ui.screens.recipe.views.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import com.cactusknights.chefbook.ui.screens.recipe.views.elements.DietElement
import com.cactusknights.chefbook.ui.themes.ChefBookTheme

@Composable
fun DietBlock(
    calories: Int?,
    macronutrients: MacronutrientsInfo?,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Text(
        text = stringResource(R.string.common_general_in_100_g),
        modifier = Modifier.padding(top = 12.dp, bottom =  2.dp),
        style = typography.caption1,
        color = colors.foregroundSecondary
    )
    Row(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        calories?.let { calories ->
            DietElement(
                name = stringResource(R.string.common_general_kcal),
                value = calories,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
        macronutrients?.protein?.let { protein ->
            DietElement(
                name = stringResource(R.string.common_general_protein),
                value = protein,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
        macronutrients?.fats?.let {
            DietElement(
                name = stringResource(R.string.common_general_fats),
                value = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
        macronutrients?.carbohydrates?.let {
            DietElement(
                name = stringResource(R.string.common_general_carbs),
                value = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )
        }
    }
    Divider(
        color = colors.backgroundTertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(1.dp)
    )
}
