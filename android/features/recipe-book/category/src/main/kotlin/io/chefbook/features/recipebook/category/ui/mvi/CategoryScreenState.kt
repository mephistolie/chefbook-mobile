package io.chefbook.features.recipebook.category.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.CollectionInput
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class CategoryScreenState(
  val name: String? = null,
  val emoji: String? = null,
  val isEditButtonAvailable: Boolean = false,
  val recipes: List<DecryptedRecipeInfo> = emptyList(),
  val cachedCollectionInput: CollectionInput? = null,
) : MviState
