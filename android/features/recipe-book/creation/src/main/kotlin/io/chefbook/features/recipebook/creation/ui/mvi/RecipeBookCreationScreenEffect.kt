package io.chefbook.features.recipebook.creation.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed class RecipeBookCreationScreenEffect : MviSideEffect {

  data object RecipeInputScreenOpened : RecipeBookCreationScreenEffect()

  data object CategoryInputScreenOpened : RecipeBookCreationScreenEffect()
}
