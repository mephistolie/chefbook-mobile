package com.cactusknights.chefbook.domain.entities.category

data class Category(
    val id: String,
    val name: String,
    val cover: String? = null,
)
