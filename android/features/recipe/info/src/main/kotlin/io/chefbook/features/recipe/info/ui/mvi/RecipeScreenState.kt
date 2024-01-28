package io.chefbook.features.recipe.info.ui.mvi

import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe
import io.chefbook.libs.mvi.MviState
import io.chefbook.features.recipe.info.ui.state.RecipeScreenBottomSheetType

internal sealed class RecipeScreenState : MviState {
  data object Loading : RecipeScreenState()

  data class Success(
    val recipe: Recipe.Decrypted,
    val servingsMultiplier: Int = recipe.servings ?: 1,
    val selectedIngredients: Set<String> = emptySet(),
    val categoriesForSelection: List<io.chefbook.sdk.category.api.external.domain.entities.Category>? = null,

    val isShareDialogVisible: Boolean = false,
    val isRemoveFromRecipeBookDialogVisible: Boolean = false,
    val isDeleteRecipeDialogVisible: Boolean = false,

    val bottomSheetType: RecipeScreenBottomSheetType = RecipeScreenBottomSheetType.MENU,
  ) : RecipeScreenState()

  class Error(val recipeId: String, val error: Throwable?) : RecipeScreenState()
}
