package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.theme.dimens.ButtonSmallHeight

@Composable
fun CaloriesBlock(
    state: RecipeInput,
    onCaloriesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
        ) {
            Text(
                text = stringResource(R.string.common_general_calories),
                style = typography.headline2,
                color = colors.foregroundPrimary,
            )
            Text(
                text = stringResource(R.string.common_recipe_input_screen_per_100_g),
                style = typography.subhead1,
                color = colors.foregroundSecondary,
            )
        }
        if (state.calories != null) {
            Row(
                modifier = Modifier
                    .simpleClickable(onClick = onCaloriesClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${state.calories} ${stringResource(R.string.common_general_kcal).lowercase()}",
                    style = typography.headline1,
                    color = colors.foregroundPrimary,
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                    tint = colors.foregroundSecondary,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(12.dp)
                        .aspectRatio(1F),
                    contentDescription = null,
                )
            }
        } else {
            DynamicButton(
                text = stringResource(R.string.common_general_specify),
                unselectedForeground = colors.foregroundPrimary,
                onClick = onCaloriesClick,
                modifier = Modifier
                    .width(128.dp)
                    .height(ButtonSmallHeight),
            )
        }
    }
}