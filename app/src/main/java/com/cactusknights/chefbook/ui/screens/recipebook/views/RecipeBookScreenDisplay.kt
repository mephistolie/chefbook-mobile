package com.cactusknights.chefbook.ui.screens.recipebook.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.domain.entities.encryption.EncryptedVaultState
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenEvent
import com.cactusknights.chefbook.ui.screens.recipebook.models.RecipeBookScreenState
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.RecipeBookActionsBlock
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.TopBar
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.allRecipesBlock
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.categoriesBlock
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.quickAccessBlock
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.dialogs.CategoryInputDialog

@Composable
fun RecipeBookScreenDisplay(
    state: RecipeBookScreenState,
    onEvent: (RecipeBookScreenEvent) -> Unit,
    topBarScale: Float = 1F,
) {
    val colors = LocalTheme.colors

    Column(
        modifier = Modifier.background(colors.backgroundPrimary)
    ) {
        TopBar(
            onCreateRecipeButtonClick = { onEvent(RecipeBookScreenEvent.OpenCreateRecipeScreen) },
            onSearchFieldClick = { onEvent(RecipeBookScreenEvent.OpenRecipeSearch) },
            onFavouriteButtonClick = { onEvent(RecipeBookScreenEvent.OpenFavouriteRecipes) },
            modifier = Modifier
                .background(colors.backgroundPrimary)
                .padding(12.dp, 12.dp, 12.dp, 6.dp),
            scale = topBarScale,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .weight(1F)
                .animateContentSize(),
        ) {
            item(
                span = { GridItemSpan(4) }
            ) {
                RecipeBookActionsBlock(
                    modifier = Modifier.padding(12.dp, 10.dp, 12.dp),
                    onCommunityRecipesButtonClick = { onEvent(RecipeBookScreenEvent.OpenCommunityRecipes) },
                    onEncryptedVaultButtonClick = { onEvent(RecipeBookScreenEvent.OpenEncryptionMenu) },
                    encryption = state.encryption ?: EncryptedVaultState.Disabled,
                )
            }
            quickAccessBlock(
                recipes = state.latestRecipes,
                onRecipeClicked = { id -> onEvent(RecipeBookScreenEvent.OpenRecipe(id)) },
            )
            categoriesBlock(
                categories = state.categories,
                isCategoriesExpanded = state.isCategoriesExpanded,
                onCategoryClicked = { id -> onEvent(RecipeBookScreenEvent.OpenCategory(id)) },
                onNewCategoryClicked = { onEvent(RecipeBookScreenEvent.ChangeNewCategoryDialogVisibility(true)) },
                onExpandClicked = { onEvent(RecipeBookScreenEvent.ChangeCategoriesExpanded) },
            )
            allRecipesBlock(
                recipes = state.allRecipes,
                categories = state.categories,
                onRecipeClicked = { id -> onEvent(RecipeBookScreenEvent.OpenRecipe(id)) },
            )
        }

        if (state.isNewCategoryDialogVisible) {
            CategoryInputDialog(
                onCancel = { onEvent(RecipeBookScreenEvent.ChangeNewCategoryDialogVisibility(false)) },
                onConfirm = { input -> onEvent(RecipeBookScreenEvent.CreateCategory(input)) }
            )
        }
    }
}
