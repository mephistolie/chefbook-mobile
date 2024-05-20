package io.chefbook.features.recipebook.categories.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

internal data class CategoriesScreenState(
  val categories: List<Category> = emptyList(),
  val tags: List<Tag> = emptyList(),
) : MviState
