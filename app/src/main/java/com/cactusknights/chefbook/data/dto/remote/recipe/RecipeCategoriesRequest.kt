package com.cactusknights.chefbook.data.dto.remote.recipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCategoriesRequest(
    @SerialName("categories")
    val categories: List<Int>,
)