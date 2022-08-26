package com.cactusknights.chefbook.data.dto.remote.recipe

import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.core.mappers.VisibilityMapper
import com.cactusknights.chefbook.data.dto.remote.categories.CategoryResponse
import com.cactusknights.chefbook.data.dto.remote.categories.toEntity
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RecipeInfoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("owner_id")
    val ownerId: Int,
    @SerialName("owner_name")
    val ownerName: String,
    @SerialName("owned")
    val isOwned: Boolean,
    @SerialName("likes")
    val likes: Int,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("encrypted")
    val isEncrypted: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("preview")
    val preview: String? = null,

    @SerialName("creation_timestamp")
    val creationTimestamp: String,
    @SerialName("update_timestamp")
    val updateTimestamp: String,

    @SerialName("saved")
    val isSaved: Boolean = false,
    @SerialName("categories")
    val categories: List<CategoryResponse>? = null,
    @SerialName("favourite")
    val isFavourite: Boolean = false,
    @SerialName("liked")
    val isLiked: Boolean = false,

    @SerialName("servings")
    val servings: Int? = null,
    @SerialName("time")
    val time: Int? = null,

    @SerialName("calories")
    val calories: Int? = null,

    )

fun RecipeInfoResponse.toEntity() : RecipeInfo =
    RecipeInfo(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        likes = likes,
        visibility = VisibilityMapper.map(visibility),
        isEncrypted = isEncrypted,
        language = LanguageMapper.map(language),
        preview = preview,

        creationTimestamp = LocalDateTime.parse(creationTimestamp, DateTimeFormatter.ISO_DATE_TIME),
        updateTimestamp = LocalDateTime.parse(updateTimestamp, DateTimeFormatter.ISO_DATE_TIME),

        isSaved = isSaved,
        categories = categories?.map { it.toEntity() } ?: emptyList(),
        isFavourite = isFavourite,
        isLiked = isLiked,

        servings = servings,
        time = time,

        calories = calories,
    )
