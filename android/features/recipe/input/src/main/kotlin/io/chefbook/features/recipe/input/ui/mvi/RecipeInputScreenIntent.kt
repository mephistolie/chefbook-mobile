package io.chefbook.features.recipe.input.ui.mvi

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.libs.mvi.MviIntent
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility

sealed class RecipeInputScreenIntent : MviIntent {
  data class Details(val data: RecipeInputDetailsScreenIntent) : RecipeInputScreenIntent()
  data class Ingredients(val data: RecipeInputIngredientsScreenIntent) : RecipeInputScreenIntent()
  data class Cooking(val data: RecipeInputCookingScreenIntent) : RecipeInputScreenIntent()

  data object Save : RecipeInputScreenIntent()

  data object CloseBottomSheet : RecipeInputScreenIntent()
  data object Back : RecipeInputScreenIntent()
  data class Close(val openRecipeScreen: Boolean = false) : RecipeInputScreenIntent()
  data object Continue : RecipeInputScreenIntent()
}

sealed class RecipeInputDetailsScreenIntent : MviIntent {
  data class SetName(val name: String) : RecipeInputDetailsScreenIntent()
  data object OpenVisibilityPicker : RecipeInputDetailsScreenIntent()
  data class SetVisibility(val visibility: Visibility) : RecipeInputDetailsScreenIntent()
  data object OpenEncryptedStatePicker : RecipeInputDetailsScreenIntent()
  data class SetEncryptedState(val isEncrypted: Boolean) : RecipeInputDetailsScreenIntent()
  data object OpenLanguagePicker : RecipeInputDetailsScreenIntent()
  data class SetLanguage(val language: Language) : RecipeInputDetailsScreenIntent()
  data class SetDescription(val description: String) : RecipeInputDetailsScreenIntent()
  data class SetPreview(val uri: String) : RecipeInputDetailsScreenIntent()
  data object RemovePreview : RecipeInputDetailsScreenIntent()
  data class SetServings(val servings: Int?) : RecipeInputDetailsScreenIntent()
  data class SetTime(val h: Int, val min: Int) : RecipeInputDetailsScreenIntent()
  data object OpenCaloriesDialog : RecipeInputDetailsScreenIntent()
  data class SetCalories(val calories: Int?) : RecipeInputDetailsScreenIntent()
  data class SetProtein(val protein: Int?) : RecipeInputDetailsScreenIntent()
  data class SetFats(val fats: Int?) : RecipeInputDetailsScreenIntent()
  data class SetCarbohydrates(val carbs: Int?) : RecipeInputDetailsScreenIntent()
}

sealed class RecipeInputIngredientsScreenIntent : MviIntent {
  object AddIngredient : RecipeInputIngredientsScreenIntent()
  object AddIngredientSection : RecipeInputIngredientsScreenIntent()
  data class OpenIngredientDialog(val ingredientId: String) : RecipeInputIngredientsScreenIntent()
  data class SetIngredientItemName(val ingredientId: String, val name: String) :
    RecipeInputIngredientsScreenIntent()

  data class SetIngredientAmount(val ingredientId: String, val amount: Int?) :
    RecipeInputIngredientsScreenIntent()

  data class SetIngredientUnit(val ingredientId: String, val unit: MeasureUnit?) :
    RecipeInputIngredientsScreenIntent()

  data class MoveIngredientItem(val from: Int, val to: Int) : RecipeInputIngredientsScreenIntent()
  data class DeleteIngredientItem(val ingredientId: String) : RecipeInputIngredientsScreenIntent()
}

sealed class RecipeInputCookingScreenIntent : MviIntent {
  object AddStep : RecipeInputCookingScreenIntent()
  object AddCookingSection : RecipeInputCookingScreenIntent()
  data class SetCookingItemValue(val index: Int, val value: String) : RecipeInputCookingScreenIntent()
  data class MoveStepItem(val from: Int, val to: Int) : RecipeInputCookingScreenIntent()
  data class AddStepPicture(val stepIndex: Int, val uri: String) : RecipeInputCookingScreenIntent()
  data class DeleteStepPicture(val stepIndex: Int, val pictureIndex: Int) :
    RecipeInputCookingScreenIntent()
  data class DeleteStepItem(val index: Int) : RecipeInputCookingScreenIntent()
}