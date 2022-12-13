package com.cactusknights.chefbook.domain.entities.category

import java.util.*

data class CategoryInput(
    val name: String,
    val cover: String? = null,
)

fun CategoryInput.toCategory(
    id: String = UUID.randomUUID().toString()
) =
    Category(
        id = id,
        name = name.trim(),
        cover = cover?.trim(),
    )
