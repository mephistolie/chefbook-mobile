package io.chefbook.sdk.recipe.crud.impl.data.models

import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput

data class RecipePictures(
  val preview: String? = null,
  val cooking: Map<String, List<String>> = emptyMap(),
) {

  fun isNotEmpty() =
    preview != null || cooking.values.any { it.isNotEmpty() }
}

fun RecipeInput.Pictures.uploaded() =
  RecipePictures(
    preview = (preview as? RecipeInput.Picture.Uploaded)?.path,
    cooking = cooking
      .mapValues { it.value.mapNotNull { picture -> (picture as? RecipeInput.Picture.Uploaded)?.path } }
      .filterValues { it.isNotEmpty() }
  )
