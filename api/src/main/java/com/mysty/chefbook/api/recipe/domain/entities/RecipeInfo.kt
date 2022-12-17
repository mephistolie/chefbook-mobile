package com.mysty.chefbook.api.recipe.domain.entities

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import java.time.LocalDateTime
import java.time.ZoneOffset

data class RecipeInfo(
    val id: String,
    val name: String,
    val ownerId: String? = null,
    val ownerName: String? = null,
    val isOwned: Boolean = false,
    val isSaved: Boolean = false,
    val likes: Int? = null,
    val visibility: Visibility = Visibility.PRIVATE,
    val encryptionState: EncryptionState = EncryptionState.Standard,
    val language: Language = Language.ENGLISH,
    val preview: String? = null,

    val creationTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    val updateTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),

    val categories: List<Category> = listOf(),
    val isFavourite: Boolean = false,
    val isLiked: Boolean = false,

    val servings: Int? = null,
    val time: Int? = null,

    val calories: Int? = null,
)

fun Recipe.toRecipeInfo(): RecipeInfo =
    RecipeInfo(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isSaved = isSaved,
        likes = likes,
        visibility = visibility,
        encryptionState = encryptionState,
        language = language,
        preview = preview,

        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,

        categories = categories,
        isFavourite = isFavourite,
        isLiked = isLiked,

        servings = servings,
        time = time,

        calories = calories,
    )
