package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import java.time.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class RecipeInput(
    val name: String = "",
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
    id: Int = 0,
    ownerId: Int? = null,
    ownerName: String? = null,
    isOwned: Boolean = true,
    isSaved: Boolean = true,
    likes: Int? = null,
    creationTimestamp: LocalDateTime = LocalDateTime.now(),
    updateTimestamp: LocalDateTime = LocalDateTime.now(),
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
        isEncrypted = isEncrypted,
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
        isEncrypted = isEncrypted,
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

fun RecipeInput.encrypt(encrypt: (ByteArray) -> String): RecipeInput {

    val name = encrypt(name.toByteArray())
    val description = description?.let { encrypt(it.toByteArray()) }
    val ingredients = encrypt(Json.encodeToString(ingredients).toByteArray())
    val cooking = encrypt(Json.encodeToString(cooking).toByteArray())

    return copy(
        name = name,
        description = description,
        ingredients = listOf(IngredientItem.EncryptedData(ingredients)),
        cooking = listOf(CookingItem.EncryptedData(cooking))
    )
}