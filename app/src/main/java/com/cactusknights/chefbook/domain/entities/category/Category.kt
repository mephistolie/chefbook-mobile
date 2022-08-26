package com.cactusknights.chefbook.domain.entities.category

data class Category(
    val id: Int,
    val name: String,
    val cover: String? = null,
)
