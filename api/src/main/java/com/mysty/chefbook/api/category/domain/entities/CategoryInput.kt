package com.mysty.chefbook.api.category.domain.entities

import java.util.UUID

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
