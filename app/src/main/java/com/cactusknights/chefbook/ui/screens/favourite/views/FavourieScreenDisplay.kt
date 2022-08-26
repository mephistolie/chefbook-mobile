package com.cactusknights.chefbook.ui.screens.favourite.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
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
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenEvent
import com.cactusknights.chefbook.ui.screens.favourite.models.FavouriteScreenState
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCard
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.Toolbar

@Composable
fun FavouriteScreenDisplay(
    state: FavouriteScreenState,
    onEvent: (FavouriteScreenEvent) -> Unit,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Box(
        modifier = Modifier
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
            .padding(horizontal = 12.dp)
            .fillMaxSize(),
    ) {
        if (state.recipes != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Toolbar(
                    onLeftButtonClick = { onEvent(FavouriteScreenEvent.Back) },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_favourite),
                            tint = colors.foregroundPrimary,
                            modifier = Modifier
                                .size(18.dp)
                                .aspectRatio(1F),
                            contentDescription = null,
                        )
                        Text(
                            text = stringResource(R.string.common_general_favourite),
                            modifier = Modifier.padding(start = 4.dp),
                            style = typography.h4,
                            color = colors.foregroundPrimary,
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    itemsIndexed(state.recipes) { index, recipe ->
                        val modifier = if (index < 2) Modifier.padding(top = 12.dp) else Modifier
                        RecipeCard(recipe, modifier) {
                            onEvent(FavouriteScreenEvent.OpenRecipeScreen(recipe.id))
                        }
                    }
                }
            }
        } else {
            CircularProgressIndicator(
                color = colors.tintPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

