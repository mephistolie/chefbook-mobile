package io.chefbook.sdk.recipe.crud.impl.data.sources.common.dto

import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable.Companion.TYPE_SECTION
import io.chefbook.sdk.recipe.core.api.internal.data.sources.common.dto.CookingItemSerializable.Companion.TYPE_STEP
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

internal fun RecipeInput.CookingItem.toSerializable() = when (this) {
  is RecipeInput.CookingItem.Step -> CookingItemSerializable(
    id = id,
    text = description,
    time = time,
    type = TYPE_STEP,
    recipeId = recipeId,
  )

  is RecipeInput.CookingItem.Section -> CookingItemSerializable(
    id = id,
    text = name,
    type = TYPE_SECTION,
  )
}
