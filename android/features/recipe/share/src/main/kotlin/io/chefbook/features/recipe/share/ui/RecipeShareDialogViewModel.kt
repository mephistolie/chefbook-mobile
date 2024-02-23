package io.chefbook.features.recipe.share.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import io.chefbook.core.android.qr.QRCodeWriter
import io.chefbook.core.android.utils.minutesToTimeString

import io.chefbook.features.recipe.share.R
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogEffect
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogIntent
import io.chefbook.features.recipe.share.ui.mvi.RecipeShareDialogState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.ui.common.extensions.localizedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import io.chefbook.core.android.R as coreR

internal typealias IRecipeShareDialogViewModel = MviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>

internal class RecipeShareDialogViewModel(
  private val recipeId: String,

  private val getRecipeUseCase: GetRecipeUseCase,
  private val qrCodeWriter: QRCodeWriter,
  context: Context,
) : BaseMviViewModel<RecipeShareDialogState, RecipeShareDialogIntent, RecipeShareDialogEffect>() {

  private val resources = context.resources

  override val _state: MutableStateFlow<RecipeShareDialogState> =
    MutableStateFlow(RecipeShareDialogState(id = recipeId, url = getRecipeLink(recipeId)))

  var textRecipe: String? = null

  override suspend fun reduceIntent(intent: RecipeShareDialogIntent) {
    when (intent) {
      is RecipeShareDialogIntent.Close -> _effect.emit(RecipeShareDialogEffect.Close)
      is RecipeShareDialogIntent.RenderQR -> renderQR(
        startColor = intent.startColor,
        endColor = intent.endColor
      )

      is RecipeShareDialogIntent.CopyLink -> copyRecipeLink()
      is RecipeShareDialogIntent.CopyAsText -> copyRecipeAsText()
    }
  }

  private fun renderQR(
    startColor: Int,
    endColor: Int,
  ) {
    _state.update { state ->
      val qr = qrCodeWriter.getGradientQrCode(
        data = state.url.orEmpty(),
        backgroundColor = Color.TRANSPARENT,
        startColor = startColor,
        endColor = endColor,
      )
      state.copy(qr = qr)
    }
  }

  private suspend fun copyRecipeLink() {
    _effect.emit(
      RecipeShareDialogEffect.CopyText(
        state.value.url.orEmpty(),
        coreR.string.common_general_link_copied
      )
    )
  }

  private suspend fun copyRecipeAsText() {
    if (textRecipe == null) {
      getRecipeUseCase(recipeId = recipeId).onSuccess { recipe ->
        textRecipe = (recipe as? DecryptedRecipe)?.asText(resources)
      }
    }
    textRecipe?.let { _effect.emit(RecipeShareDialogEffect.ShareText(it)) }
  }

  private fun getRecipeLink(recipeId: String) = "https://chefbook.io/recipes/$recipeId"
}

fun DecryptedRecipe.asText(resources: Resources): String {
  var text = name.uppercase()

  owner.name?.let { author ->
    text += "\n\n${resources.getString(coreR.string.common_general_author)}: $author"
  }

  description?.let { description ->
    text += "\n\n${resources.getString(coreR.string.common_general_description)}:\n$description"
  }

  text += "\n"
  servings?.let {
    text += "\n${resources.getString(coreR.string.common_general_servings)}: $servings"
  }
  time?.let { time ->
    text += "\n${resources.getString(coreR.string.common_general_time)}: ${
      minutesToTimeString(
        time,
        resources
      )
    }"
  }

  if (hasDietData) {
    text += "\n\n${resources.getString(coreR.string.common_general_in_100_g)}:\n"
    calories?.let { calories ->
      text += "${resources.getString(coreR.string.common_general_calories)}: $calories ${
        resources.getString(
          coreR.string.common_general_kcal
        )
      }\n"
    }
    macronutrients?.protein?.let { protein ->
      text += "${resources.getString(coreR.string.common_general_protein)}: $protein\n"
    }
    macronutrients?.fats?.let { fats ->
      text += "${resources.getString(coreR.string.common_general_fats)}: $fats\n"
    }
    macronutrients?.carbohydrates?.let { carbohydrates ->
      text += "${resources.getString(coreR.string.common_general_carbs)}: $carbohydrates"
    }
  }

  text += "\n\n${resources.getString(coreR.string.common_general_ingredients).uppercase()}\n"
  for (ingredient in ingredients) {
    when (ingredient) {
      is IngredientsItem.Section -> {
        text += "${ingredient.name}:\n"
      }
      is IngredientsItem.Ingredient -> {
        text += "• ${ingredient.name}"
        ingredient.amount?.let { amount ->
          text += " - $amount"
          ingredient.measureUnit?.let { unit ->
            text += " ${unit.localizedName(resources)}"
          }
        }
        text += "\n"
      }
      else -> Unit
    }
  }
  text += "\n\n${resources.getString(coreR.string.common_general_cooking).uppercase()}\n"
  var stepCount = 1
  for (cookingItem in cooking) {
    when (cookingItem) {
      is CookingItem.Section -> {
        text += "${cookingItem.name}:\n"
      }
      is CookingItem.Step -> {
        text += "$stepCount. ${cookingItem.description}\n"
        stepCount++
      }
      else -> Unit
    }
  }

  text += "\n${resources.getString(coreR.string.common_general_recipe)} #${id}, ${
    resources.getString(
      coreR.string.app_name
    )
  }"

  return text
}