package com.cactusknights.chefbook.ui.screens.category.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.category.toCategory
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenEvent
import com.cactusknights.chefbook.ui.screens.category.models.CategoryScreenState
import com.mysty.chefbook.design.components.dialogs.CategoryInputDialog
import com.mysty.chefbook.design.components.dialogs.TwoButtonsDialog

@Composable
fun CategoryScreenDialogs(
    state: CategoryScreenState,
    onEvent: (CategoryScreenEvent) -> Unit,
) {
    state.category?.let { category ->

        if (state.isEditCategoryDialogVisible) {
            val dialogData =
                if (state.cachedCategoryInput == null) category
                else state.cachedCategoryInput.toCategory(category.id)

            CategoryInputDialog(
                initName = dialogData.name,
                initCover = dialogData.cover,
                isEditing = true,
                onCancel = {
                    onEvent(CategoryScreenEvent.SaveEditCategoryDialogState(null))
                    onEvent(CategoryScreenEvent.ChangeDialogState.Edit(false))
                },
                onConfirm = { input ->
                    onEvent(CategoryScreenEvent.EditCategory(input))
                },
                onDelete = { input ->
                    onEvent(CategoryScreenEvent.SaveEditCategoryDialogState(input))
                    onEvent(CategoryScreenEvent.ChangeDialogState.Delete(true))
                },
            )
        }

        if (state.isDeleteCategoryDialogVisible) {
            TwoButtonsDialog(
                description = stringResource(R.string.common_category_screen_category_delete_warning),
                onHide = {
                    onEvent(CategoryScreenEvent.ChangeDialogState.Delete(false))
                },
                onRightClick = {
                    onEvent(CategoryScreenEvent.DeleteCategory)
                }
            )
        }
    }

}