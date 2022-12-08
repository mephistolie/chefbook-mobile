package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.Shapes.RoundedCornerShape12
import com.cactusknights.chefbook.ui.views.common.shimmer

@Composable
fun LatestRecipeCardSkeleton() {
    Row(
        modifier = Modifier
            .padding(end = 12.dp)
            .height(80.dp)
            .aspectRatio(2.25F)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(80.dp)
                .clip(RoundedCornerShape12)
                .shimmer()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .width(72.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .shimmer()
                )
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .shimmer()
                )
            }
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmer(),
            )
        }
    }
}
