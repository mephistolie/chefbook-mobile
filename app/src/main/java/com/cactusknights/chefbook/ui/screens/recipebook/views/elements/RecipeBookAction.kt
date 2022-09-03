package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun RecipeBookActionButton(
    title: String? = null,
    subtitle: String? = null,
    hint: String? = null,
    image: Painter? = null,
    onActionButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val pressed = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .scalingClickable(pressed, onClick = onActionButtonClick)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.backgroundSecondary)
    ) {
        image?.let {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = image,
                    modifier = imageModifier,
                    contentDescription = null,
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 10.dp, 50.dp, 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                title?.let {
                    Text(
                        text = title,
                        style = typography.subhead1,
                        color = colors.foregroundPrimary
                    )
                }
                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = typography.caption1,
                        color = colors.foregroundSecondary
                    )
                }
            }
            hint?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = hint,
                        style = typography.subhead2,
                        color = colors.foregroundSecondary
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                        tint = colors.foregroundSecondary,
                        modifier = Modifier
                            .padding(2.dp, 2.dp, 0.dp, 0.dp)
                            .size(8.dp),
                        contentDescription = null,
                    )
                }
            }
        }
        Shading(pressed.value)
    }
}
