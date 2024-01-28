package io.chefbook.sdk.recipe.book.api.external.domain.entities

import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipe

data class LatestRecipeInfo(
  val id: String,
  val name: String,
  val preview: String?,
  val time: Int?,
)

fun DecryptedRecipe.asLatestRecipeInfo() =
  LatestRecipeInfo(
    id = id,
    name = name,
    preview = preview,
    time = time,
  )
