package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import java.time.LocalDateTime

data class Recipe(
    val id: Int,
    val name: String,
    val ownerId: Int? = null,
    val ownerName: String? = null,
    val isOwned: Boolean = false,
    val isSaved: Boolean = false,
    val likes: Int? = null,
    val visibility: Visibility = Visibility.PRIVATE,
    val isEncrypted: Boolean = false,
    val language: Language = Language.OTHER,
    val description: String? = null,
    val preview: String? = null,

    val creationTimestamp: LocalDateTime = LocalDateTime.now(),
    val updateTimestamp: LocalDateTime = LocalDateTime.now(),

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
    fun hasDietDta() =
        calories != null || macronutrients?.protein != null ||
                macronutrients?.fats != null || macronutrients?.carbohydrates != null
}
