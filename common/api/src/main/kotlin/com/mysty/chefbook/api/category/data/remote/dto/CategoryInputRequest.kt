package com.mysty.chefbook.api.category.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryInputRequest(
    @SerialName("category_id")
    val categoryId: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("cover")
    val cover: String? = null,
)

internal fun CategoryInput.toRequest(categoryId: String? = null) =
    CategoryInputRequest(
        categoryId = categoryId,
        name = name,
        cover = cover,
    )
