package com.mysty.chefbook.api.recipe.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.category.data.remote.dto.CategoryResponse
import com.mysty.chefbook.api.category.data.remote.dto.toEntity
import com.mysty.chefbook.api.common.entities.language.LanguageMapper
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.visibility.VisibilityMapper
import com.mysty.chefbook.core.parseTimestampSafely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RecipeInfoResponse(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("owner_id")
    val ownerId: String,
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

internal fun RecipeInfoResponse.toEntity() : RecipeInfo =
    RecipeInfo(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        likes = likes,
        visibility = VisibilityMapper.map(visibility),
        encryptionState = if (isEncrypted) EncryptionState.Encrypted else EncryptionState.Standard,
        language = LanguageMapper.map(language),
        preview = preview,

        creationTimestamp = parseTimestampSafely(creationTimestamp),
        updateTimestamp = parseTimestampSafely(updateTimestamp),

        isSaved = isSaved,
        categories = categories?.map(CategoryResponse::toEntity) ?: emptyList(),
        isFavourite = isFavourite,
        isLiked = isLiked,

        servings = servings,
        time = time,

        calories = calories,
    )
