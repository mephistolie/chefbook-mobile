package com.cactusknights.chefbook.domain.entities.recipe

import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.toEntity
import com.cactusknights.chefbook.domain.entities.category.Category
import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.entities.recipe.encryption.EncryptionState
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.crypto.SecretKey
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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

fun Recipe.decrypt(key: SecretKey, decryptString: (String) -> ByteArray): Recipe {
    if (encryptionState != EncryptionState.Encrypted) return this

    val ingredientsData = ingredients.getOrNull(0)
    val cookingData = cooking.getOrNull(0)

    if (ingredientsData !is IngredientItem.EncryptedData || cookingData !is CookingItem.EncryptedData) return this

    val name = String(decryptString(name))
    val description = description?.let { String(decryptString(description)) }
    val ingredients: List<IngredientItemSerializable> = Json.decodeFromString(String(decryptString(ingredientsData.content)))
    val cooking: List<CookingItemSerializable> = Json.decodeFromString(String(decryptString(cookingData.content)))

    return copy(
        name = name,
        description = description,
        ingredients = ingredients.map { it.toEntity() },
        cooking = cooking.map { it.toEntity() },
        encryptionState = EncryptionState.Decrypted(key = key)
    )
}
