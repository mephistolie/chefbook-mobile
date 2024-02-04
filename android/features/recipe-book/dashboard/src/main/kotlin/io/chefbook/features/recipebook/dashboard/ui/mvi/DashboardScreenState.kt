package io.chefbook.features.recipebook.dashboard.ui.mvi

import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.category.api.external.domain.entities.Category
import io.chefbook.sdk.encryption.vault.api.external.domain.entities.EncryptedVaultState
import io.chefbook.sdk.recipe.book.api.external.domain.entities.LatestRecipeInfo
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

internal data class RecipeBookScreenState(
  val profileAvatar: String? = null,
  val onlineFeaturesAppearance: ContentAppearance = ContentAppearance.SHIMMERING,
  val encryption: EncryptedVaultState? = null,
  val latestRecipes: List<LatestRecipeInfo>? = null,
  val categories: List<Category>? = null,
  val allRecipes: List<DecryptedRecipeInfo>? = null,
) : MviState

enum class ContentAppearance {
  SHIMMERING, HIDDEN, SHOWN
}