package com.mysty.chefbook.features.recipe.info.ui.mvi

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.core.android.mvi.MviState
import com.mysty.chefbook.features.recipe.info.ui.presentation.RecipeScreenBottomSheetType

internal sealed class RecipeScreenState : MviState {
    object Loading : RecipeScreenState()

    data class Success(
      val recipe: Recipe,
      val servingsMultiplier: Int = recipe.servings ?: 1,
      val selectedIngredients: Set<String> = emptySet(),
      val categoriesForSelection: List<Category>? = null,

      val isShareDialogVisible: Boolean = false,
      val isRemoveFromRecipeBookDialogVisible: Boolean = false,
      val isDeleteRecipeDialogVisible: Boolean = false,

      val bottomSheetType: RecipeScreenBottomSheetType = RecipeScreenBottomSheetType.MENU,
    ) : RecipeScreenState()

    class Error(val recipeId: String, val error: Throwable?) : RecipeScreenState()
}
