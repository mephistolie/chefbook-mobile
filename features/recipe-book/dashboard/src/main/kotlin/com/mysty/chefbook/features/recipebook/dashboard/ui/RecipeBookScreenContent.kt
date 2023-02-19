package com.mysty.chefbook.features.recipebook.dashboard.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mysty.chefbook.api.encryption.domain.entities.EncryptedVaultState
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.theme.shapes.BottomSheetShape
import com.mysty.chefbook.features.recipebook.dashboard.ui.components.blocks.RecipeBookActionsBlock
import com.mysty.chefbook.features.recipebook.dashboard.ui.components.blocks.RecipeBookTopBar
import com.mysty.chefbook.features.recipebook.dashboard.ui.components.blocks.allRecipesBlock
import com.mysty.chefbook.features.recipebook.dashboard.ui.components.blocks.categoriesBlock
import com.mysty.chefbook.features.recipebook.dashboard.ui.components.blocks.quickAccessBlock
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenIntent
import com.mysty.chefbook.features.recipebook.dashboard.ui.mvi.RecipeBookScreenState

@Composable
internal fun RecipeBookScreenContent(
    state: RecipeBookScreenState,
    onIntent: (RecipeBookScreenIntent) -> Unit,
) {
    val colors = LocalTheme.colors

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .clippedBackground(colors.backgroundPrimary, shape = BottomSheetShape)
    ) {
        RecipeBookTopBar(
            onCreateRecipeButtonClick = { onIntent(RecipeBookScreenIntent.OpenCreateRecipeScreen) },
            onSearchFieldClick = { onIntent(RecipeBookScreenIntent.OpenRecipeSearch) },
            onFavouriteButtonClick = { onIntent(RecipeBookScreenIntent.OpenFavouriteRecipes) },
            modifier = Modifier
                .background(colors.backgroundPrimary)
                .padding(12.dp, 12.dp, 12.dp, 6.dp),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .wrapContentHeight()
                .animateContentSize(),
        ) {
            item(
                span = { GridItemSpan(4) }
            ) {
                RecipeBookActionsBlock(
                    modifier = Modifier.padding(12.dp, 10.dp, 12.dp),
                    onCommunityRecipesButtonClick = { onIntent(RecipeBookScreenIntent.OpenCommunityRecipes) },
                    onEncryptedVaultButtonClick = { onIntent(RecipeBookScreenIntent.OpenEncryptionMenu) },
                    encryption = state.encryption ?: EncryptedVaultState.Disabled,
                )
            }
            quickAccessBlock(
                recipes = state.latestRecipes,
                onRecipeClicked = { id -> onIntent(RecipeBookScreenIntent.OpenRecipe(id)) },
            )
            categoriesBlock(
                categories = state.categories,
                isCategoriesExpanded = state.isCategoriesExpanded,
                onCategoryClicked = { id -> onIntent(RecipeBookScreenIntent.OpenCategory(id)) },
                onNewCategoryClicked = { onIntent(RecipeBookScreenIntent.ChangeNewCategoryDialogVisibility(true)) },
                onExpandClicked = { onIntent(RecipeBookScreenIntent.ChangeCategoriesExpanded) },
            )
            allRecipesBlock(
                recipes = state.allRecipes,
                categories = state.categories,
                onRecipeClicked = { id -> onIntent(RecipeBookScreenIntent.OpenRecipe(id)) },
            )
        }
    }
}
