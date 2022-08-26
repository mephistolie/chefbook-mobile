package com.cactusknights.chefbook.data.dto.remote.categories

import com.cactusknights.chefbook.domain.entities.category.CategoryInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryInputRequest(
    @SerialName("name")
    val name: String,
    @SerialName("cover")
    val cover: String? = null,
)

fun CategoryInput.toRequest() =
    CategoryInputRequest(
        name = name,
        cover = cover,
    )
