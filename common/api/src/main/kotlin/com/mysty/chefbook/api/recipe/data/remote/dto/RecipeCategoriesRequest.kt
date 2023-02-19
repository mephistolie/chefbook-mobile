package com.mysty.chefbook.api.recipe.data.remote.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecipeCategoriesRequest(
    @SerialName("categories")
    val categories: List<String>,
)