package com.cactusknights.chefbook.ui.screens.category.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEvent
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenState
import com.cactusknights.chefbook.ui.screens.recipebook.views.elements.RecipeCard
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.common.Toolbar

@Composable
fun CategoryScreenDisplay(
    state: CategoryScreenState,
    onEvent: (CategoryScreenEvent) -> Unit,
) {
    val resources = LocalContext.current.resources

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 12.dp)
            .fillMaxSize(),
    ) {
        if (state.category != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Toolbar(
                    onLeftButtonClick = { onEvent(CategoryScreenEvent.Back) },
                    onContentClick = {
                        onEvent(CategoryScreenEvent.ChangeDialogState.Edit(true))
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "${state.category.cover.orEmpty()} ${state.category.name}".trim(),
                            style = typography.h4,
                            color = colors.foregroundPrimary,
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                            tint = colors.foregroundSecondary,
                            modifier = Modifier
                                .padding(start = 2.dp, top = 1.dp)
                                .size(12.dp)
                                .aspectRatio(1F),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text =
                        if (state.recipes.isNotEmpty())
                            "${stringResource(R.string.common_general_recipes)}: ${state.recipes.size}"
                        else
                            stringResource(R.string.common_general_no_recipes),
                        maxLines = 1,
                        style = typography.subhead1,
                        color = colors.foregroundSecondary,
                    )
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
                            onEvent(CategoryScreenEvent.OpenRecipeScreen(recipe.id))
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

    CategoryScreenDialogs(
        state = state,
        onEvent = onEvent,
    )

}
