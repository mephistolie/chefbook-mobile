package com.cactusknights.chefbook.data.dto.remote.recipe

import com.cactusknights.chefbook.common.parseTimestampSafely
import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.core.mappers.VisibilityMapper
import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.toEntity
import com.cactusknights.chefbook.data.dto.remote.categories.CategoryResponse
import com.cactusknights.chefbook.data.dto.remote.categories.toEntity
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RecipeResponse(
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
    @SerialName("description")
    val description: String? = null,
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
    @SerialName("macronutrients")
    val macronutrients: MacronutrientsInfoBody? = null,

    @SerialName("ingredients")
    val ingredients: List<IngredientItemSerializable>,
    @SerialName("cooking")
    val cooking: List<CookingItemSerializable>,

    )

fun RecipeResponse.toEntity() : Recipe =
    Recipe(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        likes = likes,
        visibility = VisibilityMapper.map(visibility),
        encryptionState = if (isEncrypted) EncryptionState.Encrypted else EncryptionState.Standard,
        language = LanguageMapper.map(language),
        description = description,
        preview = preview,

        creationTimestamp = parseTimestampSafely(creationTimestamp),
        updateTimestamp = parseTimestampSafely(updateTimestamp),

        isSaved = isSaved,
        categories = categories?.map { it.toEntity() } ?: emptyList(),
        isFavourite = isFavourite,
        isLiked = isLiked,

        servings = servings,
        time = time,

        calories = calories,
        macronutrients = macronutrients?.toEntity(),

        ingredients = ingredients.map { it.toEntity() },
        cooking = cooking.map { it.toEntity() },
    )
