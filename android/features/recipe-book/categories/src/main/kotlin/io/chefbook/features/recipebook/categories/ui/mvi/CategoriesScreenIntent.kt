package io.chefbook.features.recipebook.categories.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface CategoriesScreenIntent : MviIntent {
  data class CategoryClicked(val categoryId: String) : CategoriesScreenIntent

  data class TagClicked(val tagId: String) : CategoriesScreenIntent

  data object Back : CategoriesScreenIntent
}
