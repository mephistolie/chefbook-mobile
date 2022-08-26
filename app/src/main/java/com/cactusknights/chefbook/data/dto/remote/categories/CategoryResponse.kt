package com.cactusknights.chefbook.data.dto.remote.categories

import com.cactusknights.chefbook.domain.entities.category.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("cover")
    val cover: String? = null,
)

fun CategoryResponse.toEntity() =
    Category(
        id = id,
        name = name,
        cover = cover,
    )
