package io.chefbook.features.recipebook.search.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class RecipeBookSearchScreenState(
    val query: String = "",
    val categories: List<Collection> = emptyList(),
    val recipes: List<DecryptedRecipeInfo> = emptyList(),
    val showCommunitySearchHint: Boolean = false,
    val isLoading: Boolean = false,
) : MviState
