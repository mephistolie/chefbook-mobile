package com.cactusknights.chefbook.ui.screens.recipe.views.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton

@Composable
fun ActionBlock(
    state: RecipeScreenState.Success,
    onLikeClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    onShareClicked: () -> Unit,
) {
    val colors = ChefBookTheme.colors

    val recipe = state.recipe

    Divider(
        color = colors.backgroundTertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 16.dp, 12.dp, 12.dp)
            .height(1.dp)
    )
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
    ) {
        DynamicButton(
            leftIcon = ImageVector.vectorResource(R.drawable.ic_like),
            text = if (recipe.likes != null && recipe.likes > 0) recipe.likes.toString() else null,
            isSelected = recipe.isLiked,
            modifier = Modifier
                .height(44.dp)
                .wrapContentWidth(),
            onClick = onLikeClicked,
        )
        DynamicButton(
            leftIcon = ImageVector.vectorResource(R.drawable.ic_added_to_recipes),
            text = stringResource(if (recipe.isSaved) R.string.common_general_saved else R.string.common_general_save),
            isSelected = recipe.isSaved,
            modifier = Modifier
                .padding(start = 8.dp)
                .height(44.dp)
                .wrapContentWidth(),
            onClick = onSaveClicked,
        )
        AnimatedVisibility(
            visible = recipe.isSaved,
            enter = expandHorizontally(),
            exit = shrinkHorizontally(),
        ){
            DynamicButton(
                leftIcon = ImageVector.vectorResource(R.drawable.ic_favourite),
                isSelected = recipe.isFavourite,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(44.dp)
                    .wrapContentWidth(),
                onClick = onFavouriteClicked,
            )
        }
        DynamicButton(
            leftIcon = ImageVector.vectorResource(R.drawable.ic_share),
            unselectedForeground = colors.foregroundPrimary,
            modifier = Modifier
                .padding(start = 8.dp)
                .height(44.dp)
                .wrapContentWidth(),
            onClick = onShareClicked,
        )
    }
    Divider(
        color = colors.backgroundTertiary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 12.dp, 12.dp)
            .height(1.dp)
    )
}
