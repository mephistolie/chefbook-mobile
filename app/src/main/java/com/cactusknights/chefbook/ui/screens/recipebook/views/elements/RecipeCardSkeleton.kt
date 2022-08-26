package com.cactusknights.chefbook.ui.screens.recipebook.views.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.shimmer

@Composable
fun RecipeCardSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .aspectRatio(1F)
                .clip(RoundedCornerShape(16.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .padding(bottom = 6.dp)
                .width(144.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(7.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(5.dp))
                .shimmer()
        )
    }
}

@Composable
@Preview
private fun PreviewLightRecipeCardSkeleton() {
    ThemedRecipeCardSkeleton(isDarkTheme = false)
}

@Composable
@Preview
private fun PreviewDarkRecipeCardSkeleton() {
    ThemedRecipeCardSkeleton(isDarkTheme = true)
}

@Composable
private fun ThemedRecipeCardSkeleton(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            RecipeCardSkeleton()
        }
    }
}