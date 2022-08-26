package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.core.ui.scalingClickable
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.PressedRipple

@OptIn(ExperimentalUnitApi::class)
@Composable
fun CategoryCard(
    category: Category,
    modifier: Modifier = Modifier,
    onCategoryClicked: (Category) -> Unit,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val pressed = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
            .aspectRatio(0.8f)
            .scalingClickable(pressed) {
                onCategoryClicked(category)
            }
            .clip(RoundedCornerShape(16.dp))
            .background(colors.backgroundSecondary,)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = category.name,
                style = typography.subhead2,
                color = colors.foregroundPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .padding(12.dp, 8.dp, 12.dp, 0.dp),
                textAlign = TextAlign.Start,
                maxLines = 2
            )
            category.cover?.let { cover ->
                Text(
                    text = cover,
                    style = TextStyle(
                        fontSize = TextUnit(56F, TextUnitType.Sp),
                        shadow = Shadow(
                            color = Color(0x20000000),
                            blurRadius = 15F
                        )
                    ),
                    color = colors.foregroundPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2F)
                        .clip(RoundedCornerShape(16.dp))
                        .offset(12.dp, 8.dp),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
        PressedRipple(pressed.value)
    }
}
