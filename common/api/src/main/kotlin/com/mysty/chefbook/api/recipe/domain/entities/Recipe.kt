package com.mysty.chefbook.api.recipe.domain.entities

import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Recipe(
    val id: String,
    val name: String,
    val ownerId: String? = null,
    val ownerName: String? = null,
    val isOwned: Boolean = false,
    val isSaved: Boolean = false,
    val likes: Int? = null,
    val visibility: Visibility = Visibility.PRIVATE,
    val encryptionState: EncryptionState = EncryptionState.Standard,
    val language: Language = Language.OTHER,
    val description: String? = null,
    val preview: String? = null,

    val creationTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    val updateTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),

    val categories: List<Category> = emptyList(),
    val isFavourite: Boolean = false,
    val isLiked: Boolean = false,

    val servings: Int? = null,
    val time: Int? = null,

    val calories: Int? = null,
    val macronutrients: MacronutrientsInfo? = null,

    val ingredients: List<IngredientItem>,
    val cooking: List<CookingItem>,

    ) {
    fun hasDietData() =
        calories != null || macronutrients?.protein != null ||
                macronutrients?.fats != null || macronutrients?.carbohydrates != null
}
