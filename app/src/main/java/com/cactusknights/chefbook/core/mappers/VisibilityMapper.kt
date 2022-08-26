package com.cactusknights.chefbook.core.mappers

import com.cactusknights.chefbook.domain.entities.common.Visibility

object VisibilityMapper {
    private val visibilityMap = mapOf(
        "private" to Visibility.PRIVATE,
        "shared" to Visibility.SHARED,
        "public" to Visibility.PUBLIC,
    )

    fun map(visibility: String) = visibilityMap[visibility.lowercase()] ?: Visibility.PRIVATE
    fun map(visibility: Visibility) = visibility.toString().lowercase()
}
