package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.shimmer
import com.mephistolie.compost.modifiers.clippedBackground

@Composable
fun CategoryCardSkeleton() {
    val colors = ChefBookTheme.colors

    Box(
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 8.dp)
            .aspectRatio(0.8f)
            .clippedBackground(colors.backgroundSecondary, RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F)
                    .padding(12.dp, 12.dp, 12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(30.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .offset(18.dp, 6.dp)
                    .clip(RoundedCornerShape(90.dp))
                    .shimmer(),
            )
        }
    }
}
