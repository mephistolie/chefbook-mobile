package io.chefbook.features.recipebook.creation.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed class RecipeBookCreationScreenIntent : MviIntent {

  data object RecipeInputButtonClicked : RecipeBookCreationScreenIntent()

  data object CategoryInputButtonClicked : RecipeBookCreationScreenIntent()
}
