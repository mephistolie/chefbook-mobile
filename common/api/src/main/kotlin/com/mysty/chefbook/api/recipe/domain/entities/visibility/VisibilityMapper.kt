package com.mysty.chefbook.api.recipe.domain.entities.visibility

object VisibilityMapper {
    private val visibilityMap = mapOf(
        Visibility.PRIVATE.toString().lowercase() to Visibility.PRIVATE,
        Visibility.SHARED.toString().lowercase() to Visibility.SHARED,
        Visibility.PUBLIC.toString().lowercase() to Visibility.PUBLIC,
    )

    fun map(visibility: String) = visibilityMap[visibility.lowercase()] ?: Visibility.PRIVATE
}
