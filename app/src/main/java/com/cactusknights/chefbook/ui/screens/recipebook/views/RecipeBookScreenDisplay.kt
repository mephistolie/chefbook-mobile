package com.cactusknights.chefbook.ui.screens.recipebook.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.cactusknights.chefbook.ui.screens.recipebook.views.blocks.latestRecipesBlock
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.dialogs.CategoryInputDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeBookScreenDisplay(
    state: RecipeBookScreenState,
    onEvent: (RecipeBookScreenEvent) -> Unit,
    sheetProgress: Float = 1F,
) {
    val colors = ChefBookTheme.colors

    LazyColumn {
        stickyHeader {
            TopBar(
                onCreateRecipeButtonClick = { onEvent(RecipeBookScreenEvent.OpenCreateRecipeScreen) },
                onSearchFieldClick = { onEvent(RecipeBookScreenEvent.OpenRecipeSearch) },
                onFavouriteButtonClick = { onEvent(RecipeBookScreenEvent.OpenFavouriteRecipes) },
                modifier = Modifier
                    .background(colors.backgroundPrimary)
                    .padding(12.dp, 12.dp, 12.dp, 6.dp),
                sheetProgress = sheetProgress
            )
        }
        item {
            RecipeBookActionsBlock(
                modifier = Modifier.padding(12.dp, 10.dp, 12.dp),
                onCommunityRecipesButtonClick = { onEvent(RecipeBookScreenEvent.OpenCommunityRecipes) },
                onEncryptedVaultButtonClick = { onEvent(RecipeBookScreenEvent.OpenEncryptionMenu) },
                encryption = state.encryption ?: EncryptedVaultState.DISABLED,
            )
        }
        latestRecipesBlock(
            recipes = state.latestRecipes,
            onRecipeClicked = { id -> onEvent(RecipeBookScreenEvent.OpenRecipe(id)) }
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
            onRecipeClicked = { id -> onEvent(RecipeBookScreenEvent.OpenRecipe(id)) }
        )
    }

    if (state.isNewCategoryDialogVisible) {
        CategoryInputDialog(
            onCancel = { onEvent(RecipeBookScreenEvent.ChangeNewCategoryDialogVisibility(false)) },
            onConfirm = { input -> onEvent(RecipeBookScreenEvent.CreateCategory(input)) }
        )
    }

}
