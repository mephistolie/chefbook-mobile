package com.cactusknights.chefbook.ui.screens.search.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.core.ui.scalingClickable
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.PressedRipple

@Composable
fun SearchRecipeCard(
    recipe: RecipeInfo,
    modifier: Modifier = Modifier,
    onRecipeClicked: (RecipeInfo) -> Unit,
) {
    val context = LocalContext.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val pressed = remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .scalingClickable(pressed) { onRecipeClicked(recipe) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.backgroundSecondary)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(recipe.preview ?: R.drawable.ic_broccy)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            PressedRipple(pressed.value)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Text(
                text = recipe.name,
                style = typography.headline1,
                maxLines = 2,
                color = colors.foregroundPrimary,
            )
            recipe.time?.let {
                Text(
                    text = Utils.minutesToTimeString(recipe.time, context.resources),
                    style = typography.body2,
                    color = colors.foregroundSecondary
                )
            }
        }
    }
}
