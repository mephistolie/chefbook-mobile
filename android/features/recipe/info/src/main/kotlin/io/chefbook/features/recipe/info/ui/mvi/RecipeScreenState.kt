package io.chefbook.features.recipe.info.ui.mvi

import io.chefbook.features.recipe.info.ui.state.RecipeScreenBottomSheetType
import io.chefbook.libs.mvi.MviState
import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe

internal sealed class RecipeScreenState : MviState {
  data object Loading : RecipeScreenState()

  data class Success(
      val recipe: Recipe.Decrypted,
      val servingsMultiplier: Int = recipe.servings ?: 1,
      val selectedIngredients: Set<String> = emptySet(),
      val categoriesForSelection: List<Collection>? = null,

      val isShareDialogVisible: Boolean = false,
      val isRemoveFromRecipeBookDialogVisible: Boolean = false,
      val isDeleteRecipeDialogVisible: Boolean = false,

      val bottomSheetType: RecipeScreenBottomSheetType = RecipeScreenBottomSheetType.MENU,
  ) : RecipeScreenState()

  class Error(val recipeId: String, val error: Throwable?) : RecipeScreenState()
}
