package io.chefbook.features.recipe.input.ui.screens.cooking.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput.CookingItem
import io.chefbook.features.recipe.input.ui.mvi.RecipeInputCookingScreenIntent
import io.chefbook.features.recipe.input.ui.screens.ingredients.components.SectionField
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState

internal fun LazyListScope.cookingItemList(
  cooking: List<CookingItem>,
  onIntent: (RecipeInputCookingScreenIntent) -> Unit,
  reorderableState: ReorderableLazyListState,
  haptic: HapticFeedback,
  onFocusStep: (Int) -> Unit,
  imagePickerLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
  itemsIndexed(cooking, key = { _, item -> item.id }) { index, item ->
    ReorderableItem(
      reorderableState = reorderableState,
      key = item.id,
    ) { isDragging ->
      if (isDragging) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
      when (item) {
        is CookingItem.Section -> {
          SectionField(
            name = item.name,
            onNameChange = { name ->
              onIntent(RecipeInputCookingScreenIntent.SetCookingItemValue(index, name))
            },
            onDeleteClick = { onIntent(RecipeInputCookingScreenIntent.DeleteStepItem(index)) },
          )
        }
        is CookingItem.Step -> {
          StepField(
            step = item,
            number = cooking.subList(0, index).filterIsInstance<CookingItem.Step>().size + 1,
            onDescriptionChange = { name ->
              onIntent(RecipeInputCookingScreenIntent.SetCookingItemValue(index, name))
            },
            onAddPictureClick = {
              onFocusStep(index)
              imagePickerLauncher.launch("image/*")
            },
            onDeletePictureClick = { pictureIndex ->
              onIntent(RecipeInputCookingScreenIntent.DeleteStepPicture(
                  stepIndex = index,
                  pictureIndex = pictureIndex
                )
              )
            },
            onDeleteClick = { onIntent(RecipeInputCookingScreenIntent.DeleteStepItem(index)) },
          )
        }
        else -> Unit
      }
    }
  }
}