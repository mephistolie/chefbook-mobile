package io.chefbook.features.category.ui.input.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class CategoryInputScreenIntent : MviIntent {
  data object Cancel : CategoryInputScreenIntent()
  data class SetName(val name: String) : CategoryInputScreenIntent()
  data class SetCover(val cover: String) : CategoryInputScreenIntent()
  data object ConfirmInput : CategoryInputScreenIntent()
  data object Delete : CategoryInputScreenIntent()
  data object ConfirmDelete : CategoryInputScreenIntent()
}
