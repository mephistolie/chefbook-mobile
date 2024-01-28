package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.category.api.external.domain.entities.CategoryInput

internal data class CategoryScreenState(
  val category: Category? = null,
  val recipes: List<DecryptedRecipeInfo> = emptyList(),
  val cachedCategoryInput: CategoryInput? = null,
) : MviState
