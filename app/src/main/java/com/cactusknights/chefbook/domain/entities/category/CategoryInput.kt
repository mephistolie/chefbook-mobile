package com.cactusknights.chefbook.domain.entities.category

data class CategoryInput(
    val name: String,
    val cover: String? = null,
)

fun CategoryInput.toCategory(
    id: Int = 0
) =
    Category(
        id = id,
        name = name.trim(),
        cover = cover?.trim(),
    )
