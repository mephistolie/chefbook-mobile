package com.mysty.chefbook.api.category.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.category.domain.entities.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryResponse(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("cover")
    val cover: String? = null,
)

internal fun CategoryResponse.toEntity() =
    Category(
        id = id,
        name = name,
        cover = cover,
    )
