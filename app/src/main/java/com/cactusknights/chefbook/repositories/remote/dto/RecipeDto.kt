package com.cactusknights.chefbook.repositories.remote.dto

import com.cactusknights.chefbook.common.RoomConverters
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.local.entities.toRecipeEntity
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

data class RecipeDto(
    var id: Int? = null,
    var name: String,
    var likes: Int = 0,
    @SerializedName("owner_id") var ownerId: Int = 0,
    @SerializedName("owner_name") var ownerName: String = "",
    @SerializedName("owned") var isOwned: Boolean = false,
    @SerializedName("favourite") var isFavourite: Boolean = false,
    @SerializedName("liked") var isLiked: Boolean = false,

    var description: String? = null,
    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,
    var categories: ArrayList<Int> = arrayListOf(),

    var ingredients: Any,
    var cooking: Any,

    var visibility: String = "private",
    var encrypted: Boolean = false,
    var preview: String? = null,
    @SerializedName("creation_timestamp") var creationTimestamp: Date = Date(),
    @SerializedName("update_timestamp") var updateTimestamp: Date = Date()
)

data class RecipeInputDto(
    var name: String,
    @SerializedName("owner_id") var ownerId: Int = 0,

    var description: String? = null,
    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,
    var categories: ArrayList<Int> = arrayListOf(),

    var ingredients: Any,
    var cooking: Any,

    var visibility: String = "private",
    var preview: String? = null
)

data class DeleteRecipePictureInput(
    @SerializedName("picture_name") var pictureName: String
)

fun RecipeDto.toRecipe(): Recipe {
    try {
        val type: Type = object : TypeToken<ArrayList<MarkdownString>>() {}.type
        return if (!encrypted) DecryptedRecipe(
            remoteId = id,
            name = name,
            likes = likes,
            ownerId = ownerId,
            ownerName = ownerName,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            description = description,
            servings = servings,
            calories = calories,
            visibility = Visibility.getByString(visibility),
            categories = if (categories != null) categories else arrayListOf(),
            time = time,
            ingredients = Gson().fromJson(ingredients as JsonElement, type),
            cooking = Gson().fromJson(cooking as JsonElement, type),
            preview = preview,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp,
        ) else EncryptedRecipe(
            remoteId = id,
            name = name,
            likes = likes,
            ownerId = ownerId,
            ownerName = ownerName,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            description = description,
            servings = servings,
            calories = calories,
            visibility = Visibility.getByString(visibility),
            categories = if (categories != null) categories else arrayListOf(),
            time = time,
            ingredients = ingredients as String,
            cooking = cooking as String,
            preview = preview,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp,
        )
    } catch (e: Exception) {
        throw IOException()
    }
}

fun Recipe.toRecipeInputDto(): RecipeInputDto {
    if (this is DecryptedRecipe) return RecipeInputDto(
        name = name,
        description = description,
        servings = servings,
        calories = calories,
        time = time,
        categories = categories,
        visibility = visibility.name.lowercase(),
        preview = preview,
        ingredients = Gson().toJsonTree(ingredients),
        cooking = Gson().toJsonTree(cooking),
    ) else if (this is EncryptedRecipe) return RecipeInputDto(
        name = name,
        description = description,
        servings = servings,
        calories = calories,
        time = time,
        categories = categories,
        visibility = visibility.name.lowercase(),
        preview = preview,
        ingredients = ingredients,
        cooking = cooking,
    ) else throw IOException()
}