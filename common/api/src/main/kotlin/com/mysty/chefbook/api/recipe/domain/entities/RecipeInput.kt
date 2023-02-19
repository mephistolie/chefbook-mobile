package com.mysty.chefbook.api.recipe.domain.entities

import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import com.mysty.chefbook.core.constants.Strings
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

data class RecipeInput(
    val name: String = Strings.EMPTY,
    val visibility: Visibility = Visibility.PRIVATE,
    val isEncrypted: Boolean = false,
    val language: Language = Language.ENGLISH,
    val description: String? = null,
    val preview: String? = null,

    val servings: Int? = 1,
    val time: Int? = null,

    val calories: Int? = null,
    val macronutrients: MacronutrientsInfo? = null,

    val ingredients: List<IngredientItem> = emptyList(),
    val cooking: List<CookingItem> = emptyList(),
)

fun RecipeInput.toRecipe(
    id: String = UUID.randomUUID().toString(),
    ownerId: String? = null,
    ownerName: String? = null,
    isOwned: Boolean = true,
    isSaved: Boolean = true,
    likes: Int? = null,
    creationTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    updateTimestamp: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    isFavourite: Boolean = false,
    isLiked: Boolean = false,
) =
    Recipe(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isSaved = isSaved,
        likes = likes,
        visibility = visibility,
        encryptionState = if (isEncrypted) EncryptionState.Encrypted else EncryptionState.Standard,
        language = language,
        description = description,
        preview = preview,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        isFavourite = isFavourite,
        isLiked = isLiked,
        servings = servings,
        time = time,

        calories = calories,
        macronutrients = macronutrients,

        ingredients = ingredients,
        cooking = cooking,
    )

fun Recipe.toRecipeInput() =
    RecipeInput(
        name = name,
        visibility = visibility,
        isEncrypted = encryptionState != EncryptionState.Standard,
        language = language,
        description = description,
        preview = preview,
        servings = servings,
        time = time,

        calories = calories,
        macronutrients = macronutrients,

        ingredients = ingredients,
        cooking = cooking,
    )

fun RecipeInput.withoutPictures(): RecipeInput =
    this.copy(
        preview = null,
        cooking = cooking.map { if (it is CookingItem.Step) it.copy(pictures = null) else it }
    )
