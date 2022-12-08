package com.cactusknights.chefbook.ui.screens.recipe.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.main.models.UiState
import com.cactusknights.chefbook.ui.screens.recipe.dialogs.share.RecipeShareDialog
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipePicturesDialogState
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenEvent
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenState
import com.cactusknights.chefbook.ui.views.dialogs.FullscreenPicturesDialog
import com.cactusknights.chefbook.ui.views.dialogs.TwoButtonsDialog

@Composable
fun RecipeScreenDialogs(
    state: RecipeScreenState.Success,
    onEvent: (RecipeScreenEvent) -> Unit,
) {

    if (state.isShareDialogVisible) {
        RecipeShareDialog(
            recipe = state.recipe,
            onHide = { onEvent(RecipeScreenEvent.ChangeDialogState.Share(false)) }
        )
    }
    if (state.picturesDialogState is RecipePicturesDialogState.Visible) {
        FullscreenPicturesDialog(
            pictures = state.picturesDialogState.pictures,
            startIndex = state.picturesDialogState.startIndex,
            onHide = { onEvent(RecipeScreenEvent.ChangeDialogState.Pictures(false)) },
        )
    }
    if (state.isRemoveFromRecipeBookDialogVisible) {
        TwoButtonsDialog(
            description = stringResource(R.string.common_recipe_screen_unsaving_warning),
            onHide = { onEvent(RecipeScreenEvent.ChangeDialogState.RemoveFromRecipeBook(false)) },
            onRightClick = { onEvent(RecipeScreenEvent.ChangeSavedStatus) },
        )
    }
    if (state.isDeleteRecipeDialogVisible) {
        TwoButtonsDialog(
            description = stringResource(R.string.common_recipe_screen_delete_warning),
            onHide = { onEvent(RecipeScreenEvent.ChangeDialogState.Delete(false)) },
            onRightClick = { onEvent(RecipeScreenEvent.DeleteRecipe) },
        )
    }

    LaunchedEffect(key1 = state) {
        UiState.backgroundBlur.value =
            if (state.isShareDialogVisible || state.picturesDialogState is RecipePicturesDialogState.Visible
                || state.isRemoveFromRecipeBookDialogVisible || state.isDeleteRecipeDialogVisible) 30.dp else 0.dp
    }
}