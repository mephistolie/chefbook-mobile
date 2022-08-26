package com.cactusknights.chefbook.data.dto.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.core.mappers.VisibilityMapper
import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.toEntity
import com.cactusknights.chefbook.data.dto.common.recipe.toSerializable
import com.cactusknights.chefbook.data.dto.local.room.RecipeRoom.Companion.TABLE_NAME
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import java.time.LocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["recipe_id"], unique = true)])
data class RecipeRoom (
    
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipe_id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "owner_id")
    val ownerId: Int? = null,
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
}

fun RecipeRoom.toEntity(): Recipe {
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
        isEncrypted = isEncrypted,
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


fun Recipe.toRoom() =
    RecipeRoom(
        id = id,
        name = name,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isSaved = isSaved,
        likes = likes,
        visibility = VisibilityMapper.map(visibility),
        isEncrypted = isEncrypted,
        language = LanguageMapper.map(language),
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

        ingredients = Json.encodeToString(ingredients.map { it.toSerializable() }),
        cooking = Json.encodeToString(cooking.map { it.toSerializable() }),
    )
