package io.chefbook.features.recipebook.creation.ui

import io.chefbook.features.recipebook.creation.ui.mvi.RecipeBookCreationScreenEffect
import io.chefbook.features.recipebook.creation.ui.mvi.RecipeBookCreationScreenIntent
import io.chefbook.libs.mvi.BaseIntentSideEffectViewModel

internal class RecipeBookCreationScreenViewModel :
  BaseIntentSideEffectViewModel<RecipeBookCreationScreenIntent, RecipeBookCreationScreenEffect>() {

  override suspend fun reduceIntent(intent: RecipeBookCreationScreenIntent) {
    when (intent) {
      is RecipeBookCreationScreenIntent.RecipeInputButtonClicked ->
        _effect.emit(RecipeBookCreationScreenEffect.RecipeInputScreenOpened)

      is RecipeBookCreationScreenIntent.CategoryInputButtonClicked ->
        _effect.emit(RecipeBookCreationScreenEffect.CategoryInputScreenOpened)
    }
  }
}
