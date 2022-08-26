package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.Counter

@Composable
fun ServingsBlock(
    state: RecipeInput,
    onSetServings: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.common_general_servings),
            style = typography.headline1,
            color = colors.foregroundPrimary,
        )
        Counter(
            count = state.servings ?: 0,
            isTextEditable = true,
            onValueChange = { value -> onSetServings(value.toIntOrNull()) },
            onMinusClicked = { onSetServings((state.servings ?: 0) - 1) },
            onPlusClicked = { onSetServings((state.servings ?: 0) + 1) },
        )
    }
}