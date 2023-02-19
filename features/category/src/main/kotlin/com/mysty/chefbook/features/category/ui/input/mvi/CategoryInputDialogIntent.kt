package com.mysty.chefbook.features.category.ui.input.mvi

import com.mysty.chefbook.core.android.mvi.MviIntent

internal sealed class CategoryInputDialogIntent : MviIntent {
  object Cancel : CategoryInputDialogIntent()
  data class SetName(val name: String) : CategoryInputDialogIntent()
  data class SetCover(val cover: String) : CategoryInputDialogIntent()
  object ConfirmInput : CategoryInputDialogIntent()
  object Delete : CategoryInputDialogIntent()
  object ConfirmDelete : CategoryInputDialogIntent()
}
