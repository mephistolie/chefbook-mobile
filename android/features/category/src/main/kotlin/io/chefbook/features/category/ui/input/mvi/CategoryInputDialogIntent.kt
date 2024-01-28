package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class CategoryInputDialogIntent : MviIntent {
  data object Cancel : CategoryInputDialogIntent()
  data class SetName(val name: String) : CategoryInputDialogIntent()
  data class SetCover(val cover: String) : CategoryInputDialogIntent()
  data object ConfirmInput : CategoryInputDialogIntent()
  data object Delete : CategoryInputDialogIntent()
  data object ConfirmDelete : CategoryInputDialogIntent()
}
