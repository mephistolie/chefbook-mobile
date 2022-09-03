package com.cactusknights.chefbook.ui.screens.recipe.views.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun CookingStep(
    number: Int,
    step: CookingItem.Step,
    onStepPictureClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier,
    ) {
        Text(
            text = "$number.",
            style = typography.body1,
            color = colors.tintPrimary,
            modifier = Modifier.width(26.dp)
        )
        Column {
            Text(
                text = step.description,
                style = typography.headline1,
                color = colors.foregroundPrimary,
            )
            step.pictures?.let { pictures ->
                for (i in pictures.indices step PICTURE_ROW_CELLS_COUNT) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (j in i until i + PICTURE_ROW_CELLS_COUNT) {
                            if (j < pictures.size) {
                                val pressed = remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .aspectRatio(1.25F)
                                        .height(24.dp)
                                        .scalingClickable(
                                            pressed = pressed,
                                            onClick = { onStepPictureClicked(pictures[j]) }
                                        )
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(pictures[j])
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize(),
                                    )
                                    Shading(isVisible = pressed.value)
                                }
                            } else {
                                Spacer(modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private const val PICTURE_ROW_CELLS_COUNT = 3