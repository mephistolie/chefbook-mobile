package io.chefbook.features.recipe.input.ui.viewmodel

import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.CookingItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

internal fun DecryptedRecipe.toInput() =
  RecipeInput(
    id = id,
    name = name,
    visibility = visibility,
    hasEncryption = isEncryptionEnabled,
    language = language,
    description = description,
    preview = preview?.let(RecipeInput.Picture::Uploaded),
    servings = servings,
    time = time,

    calories = calories,
    macronutrients = macronutrients,

    ingredients = ingredients.map(IngredientsItem::toInput),
    cooking = cooking.map(CookingItem::toInput),

    version = version,
  )

private fun IngredientsItem.toInput() =
  when (this) {
    is IngredientsItem.Ingredient -> IngredientsItem.Ingredient(
      id = id,
      name = name,
      amount = amount,
      measureUnit = measureUnit,
      recipeId = recipeId,
    )

    is IngredientsItem.Section -> IngredientsItem.Section(
      id = id,
      name = name,
    )
  }

private fun CookingItem.toInput() =
  when (this) {
    is CookingItem.Step -> RecipeInput.CookingItem.Step(
      id = id,
      description = description,
      time = time,
      pictures = pictures.map(RecipeInput.Picture::Uploaded),
      recipeId = recipeId,
    )

    is CookingItem.Section -> RecipeInput.CookingItem.Section(
      id = id,
      name = name,
    )
  }