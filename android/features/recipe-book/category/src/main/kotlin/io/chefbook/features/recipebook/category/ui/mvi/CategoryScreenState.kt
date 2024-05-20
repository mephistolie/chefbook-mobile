package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class CategoryScreenState(
  val name: String? = null,
  val emoji: String? = null,
  val isEditButtonAvailable: Boolean = false,
  val recipes: List<DecryptedRecipeInfo> = emptyList(),
  val cachedCategoryInput: CategoryInput? = null,
) : MviState
