package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Visibility
import java.time.LocalDateTime

data class RecipeInfo(
    val id: Int,
    val name: String,
    val ownerId: Int? = null,
    val ownerName: String? = null,
    val isOwned: Boolean = false,
    val isSaved: Boolean = false,
    val likes: Int? = null,
    val visibility: Visibility = Visibility.PRIVATE,
    val isEncrypted: Boolean = false,
    val language: Language = Language.ENGLISH,
    val preview: String? = null,

    val creationTimestamp: LocalDateTime = LocalDateTime.now(),
    val updateTimestamp: LocalDateTime = LocalDateTime.now(),

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
        isEncrypted = isEncrypted,
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
