package com.mysty.chefbook.api.category.domain.entities

import com.mysty.chefbook.core.constants.Strings
import java.util.UUID

data class CategoryInput(
    val name: String = Strings.EMPTY,
    val cover: String? = null,
)

fun Category.toInput() =
    CategoryInput(
        name = name.trim(),
        cover = cover?.trim(),
    )

fun CategoryInput.toCategory(
    id: String = UUID.randomUUID().toString()
) =
    Category(
        id = id,
        name = name.trim(),
        cover = cover?.trim(),
    )
