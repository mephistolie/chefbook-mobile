package io.chefbook.sdk.recipe.book.api.external.domain.entities

import io.chefbook.sdk.collection.api.external.domain.entities.Collection
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeInfo

data class RecipeBook(
    val recipes: List<RecipeInfo>,
    val collections: List<Collection>,
)
