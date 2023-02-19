package com.mysty.chefbook.api.recipe.domain.entities.visibility

enum class Visibility {
    PRIVATE, SHARED, PUBLIC;

    val code = toString().lowercase()
}
