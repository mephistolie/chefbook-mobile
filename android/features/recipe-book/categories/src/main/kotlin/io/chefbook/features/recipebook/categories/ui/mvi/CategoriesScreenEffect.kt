package io.chefbook.features.recipebook.categories.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface CategoriesScreenEffect : MviSideEffect {
  data class CategoryOpened(val categoryId: String) : CategoriesScreenEffect

  data class TagOpened(val tagId: String): CategoriesScreenEffect

  data object Back : CategoriesScreenEffect
}
