package com.mysty.chefbook.api.recipe.data.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mysty.chefbook.api.common.entities.language.LanguageMapper
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.IngredientItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.toSerializable
import com.mysty.chefbook.api.recipe.data.local.tables.RecipeRoomEntity.Companion.TABLE_NAME
import com.mysty.chefbook.api.recipe.domain.entities.Recipe
import com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import com.mysty.chefbook.api.recipe.domain.entities.visibility.VisibilityMapper
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["recipe_id"], unique = true)])
internal data class RecipeRoomEntity (
    
    @PrimaryKey
    @ColumnInfo(name = "recipe_id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "owner_id")
    val ownerId: String? = null,
    @ColumnInfo(name = "owner_name")
    val ownerName: String? = null,
    @ColumnInfo(name = "owned")
    val isOwned: Boolean,
    @ColumnInfo(name = "saved")
    val isSaved: Boolean,
    @ColumnInfo(name = "likes")
    val likes: Int? = null,
    @ColumnInfo(name = "visibility")
    val visibility: String,
    @ColumnInfo(name = "encrypted")
    val isEncrypted: Boolean,
    @ColumnInfo(name = "language")
    val language: String = "en",
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "preview")
    val preview: String? = null,

    @ColumnInfo(name = "creation_timestamp")
    val creationTimestamp: String,
    @ColumnInfo(name = "update_timestamp")
    val updateTimestamp: String,

    @ColumnInfo(name = "favourite")
    val isFavourite: Boolean = false,
    @ColumnInfo(name = "liked")
    val isLiked: Boolean = false,

    @ColumnInfo(name = "servings")
    val servings: Int? = null,
    @ColumnInfo(name = "time")
    val time: Int? = null,

    @ColumnInfo(name = "calories")
    val calories: Int? = null,
    @ColumnInfo(name = "protein")
    val protein: Int? = null,
    @ColumnInfo(name = "fats")
    val fats: Int? = null,
    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Int? = null,

    @ColumnInfo(name = "ingredients")
    val ingredients: String,
    @ColumnInfo(name = "cooking")
    val cooking: String,
) {
    companion object {
        const val TABLE_NAME = "recipes"
    }

    fun toEntity(): Recipe {
        var macronutrients: MacronutrientsInfo? = null
        if (protein != null || fats != null || carbohydrates != null) {
            macronutrients = MacronutrientsInfo(protein, fats, carbohydrates)
        }

        val ingredients: List<IngredientItemSerializable> = Json.decodeFromString(ingredients)
        val cooking: List<CookingItemSerializable> = Json.decodeFromString(cooking)

        return Recipe(
            id = id,
            name = name,
            ownerId = ownerId,
            ownerName = ownerName,
            isOwned = isOwned,
            isSaved = isSaved,
            likes = likes,
            visibility = VisibilityMapper.map(visibility),
            encryptionState = if (isEncrypted) EncryptionState.Encrypted else EncryptionState.Standard,
            language = LanguageMapper.map(language),
            description = description,
            preview = preview,
            creationTimestamp = LocalDateTime.parse(creationTimestamp),
            updateTimestamp = LocalDateTime.parse(updateTimestamp),
            isFavourite = isFavourite,
            isLiked = isLiked,
            servings = servings,
            time = time,

            calories = calories,
            macronutrients = macronutrients,

            ingredients = ingredients.map { it.toEntity() },
            cooking = cooking.map { it.toEntity() },
        )
    }
}


internal fun Recipe.toRoom() =
    RecipeRoomEntity(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isSaved = isSaved,
        likes = likes,
        visibility = visibility.code,
        isEncrypted = encryptionState != com.mysty.chefbook.api.recipe.domain.entities.encryption.EncryptionState.Standard,
        language = language.code,
        description = description,
        preview = preview,
        creationTimestamp = creationTimestamp.toString(),
        updateTimestamp = updateTimestamp.toString(),
        isFavourite = isFavourite,
        isLiked = isLiked,
        servings = servings,
        time = time,

        calories = calories,
        protein = macronutrients?.protein,
        fats = macronutrients?.fats,
        carbohydrates = macronutrients?.carbohydrates,

        ingredients = Json.encodeToString(ingredients.map(com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem::toSerializable)),
        cooking = Json.encodeToString(cooking.map(com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem::toSerializable)),
    )
