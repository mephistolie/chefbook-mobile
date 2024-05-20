package io.chefbook.features.recipebook.dashboard.ui

import androidx.lifecycle.viewModelScope
import io.chefbook.features.recipebook.dashboard.ui.mvi.ContentAppearance
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenEffect
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenIntent
import io.chefbook.features.recipebook.dashboard.ui.mvi.DashboardScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.encryption.vault.api.external.domain.usecases.ObserveEncryptedVaultStateUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveLatestRecipesUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

internal class DashboardScreenViewModel(
  private val observeProfileUseCase: ObserveProfileUseCase,
  private val observeRecipeBookUseCase: ObserveRecipeBookUseCase,
  private val observeLatestRecipesUseCase: ObserveLatestRecipesUseCase,
  private val observeEncryptionUseCase: ObserveEncryptedVaultStateUseCase,
) : BaseMviViewModel<DashboardScreenState, DashboardScreenIntent, DashboardScreenEffect>() {

  override val _state = MutableStateFlow(DashboardScreenState())

  init {
    observeRecipes()
  }

  private fun observeRecipes() {
    combine(
      observeProfileUseCase(),
      observeRecipeBookUseCase(),
      observeLatestRecipesUseCase(),
      observeEncryptionUseCase(),
    ) { profile, recipeBook, latestRecipes, encryption ->
      val allRecipes = recipeBook?.recipes
        ?.filterIsInstance<DecryptedRecipeInfo>()
        ?.sortedWith(compareBy({ it.name.uppercase() }, { it.id }))
      val categories = recipeBook?.categories
        ?.sortedBy { it.name }

      _state.update { state ->
        state.copy(
          profileAvatar = profile?.avatar,
          onlineFeaturesAppearance = when {
            profile?.isOnline == true -> ContentAppearance.SHOWN
            profile != null -> ContentAppearance.HIDDEN
            else -> ContentAppearance.SHIMMERING
          },
          allRecipes = allRecipes,
          latestRecipes = latestRecipes,
          categories = categories,
          encryption = encryption,
        )
      }
    }
      .launchIn(viewModelScope)
  }

  override suspend fun reduceIntent(intent: DashboardScreenIntent) {
    when (intent) {
      is DashboardScreenIntent.ProfileButtonClicked ->
        _effect.emit(DashboardScreenEffect.ProfileScreenOpened)

      is DashboardScreenIntent.CreateButtonClicked ->
        _effect.emit(DashboardScreenEffect.CreationScreenOpened)

      is DashboardScreenIntent.OpenRecipeSearch ->
        _effect.emit(DashboardScreenEffect.RecipeSearchScreenOpened)

      is DashboardScreenIntent.OpenCommunityRecipes ->
        _effect.emit(DashboardScreenEffect.CommunityRecipesScreenOpened)

      is DashboardScreenIntent.OpenEncryptionMenu ->
        _effect.emit(DashboardScreenEffect.OpenEncryptedVaultScreen)

      is DashboardScreenIntent.OpenShoppingListScreen ->
        _effect.emit(DashboardScreenEffect.ShoppingListScreenOpened)

      is DashboardScreenIntent.OpenFavouriteRecipes ->
        _effect.emit(DashboardScreenEffect.FavouriteRecipesScreenOpened)

      is DashboardScreenIntent.OpenCategory ->
        _effect.emit(DashboardScreenEffect.CategoryRecipesScreenOpened(intent.categoryId))

      is DashboardScreenIntent.OpenRecipe ->
        _effect.emit(DashboardScreenEffect.RecipeScreenOpened(intent.recipeId))

      is DashboardScreenIntent.OpenCategories ->
        _effect.emit(DashboardScreenEffect.CategoriesScreenOpened)
    }
  }
}
