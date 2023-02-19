package com.mysty.chefbook.api.category.domain.entities

data class Category(
    val id: String,
    val name: String,
    val cover: String? = null,
)
