package com.cactusknights.chefbook.data.sources.remote.dto

import com.cactusknights.chefbook.models.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

data class RecipeDto(
    val id: Int? = null,
    val name: String,
    val likes: Int = 0,
    @SerializedName("owner_id") var ownerId: Int = 0,
    @SerializedName("owner_name") var ownerName: String = "",
    @SerializedName("owned") var isOwned: Boolean = false,
    @SerializedName("favourite") var isFavourite: Boolean = false,
    @SerializedName("liked") var isLiked: Boolean = false,

    val description: String? = null,
    val servings: Int = 1,
    val time: Int = 15,
    val calories: Int = 0,
    val categories: ArrayList<Int> = arrayListOf(),

    val ingredients: Any,
    val cooking: Any,

    val visibility: String = "private",
    val encrypted: Boolean = false,
    val preview: String? = null,
    @SerializedName("creation_timestamp") var creationTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    @SerializedName("update_timestamp") var updateTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
)

data class RecipeInputDto(
    val name: String,
    @SerializedName("owner_id") var ownerId: Int = 0,

    val description: String? = null,
    val servings: Int = 1,
    val time: Int = 15,
    val calories: Int = 0,
    val categories: ArrayList<Int>? = null,

    val ingredients: Any,
    val cooking: Any,

    val visibility: String = "private",
    val preview: String? = null,
    val encrypted: Boolean
)

data class DeleteRecipePictureInput(
    @SerializedName("picture_name") var pictureName: String
)

@Suppress("UNCHECKED_CAST")
fun RecipeDto.toRecipe(): Recipe {
    try {
        if (!encrypted) {
            val ingredientsMap = ingredients as ArrayList<LinkedTreeMap<String, Any>>
            val ingredients = arrayListOf<Ingredient>()
            for (ingredientMap in ingredientsMap) {
                val ingredient = Ingredient(
                    text = ingredientMap["text"]!! as String,
                    link = ingredientMap["link"] as String?,
                    type = enumValueOf(ingredientMap["type"]!! as String))
                ingredients.add(ingredient)
            }
            val cookingMap = cooking as ArrayList<LinkedTreeMap<String, Any>>
            val cooking = arrayListOf<CookingStep>()
            for (stepMap in cookingMap) {
                val step = CookingStep(
                    text = stepMap["text"]!! as String,
                    link = stepMap["link"] as String?,
                    pictures = stepMap["pictures"] as? ArrayList<String>?: arrayListOf(),
                    type = enumValueOf(stepMap["type"]!! as String))
                cooking.add(step)
            }
            return DecryptedRecipe(
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
                categories = if (!categories.isNullOrEmpty()) categories else arrayListOf(),
                time = time,
                ingredients = ingredients,
                cooking = cooking,
                preview = preview,
                creationTimestamp = creationTimestamp,
                updateTimestamp = updateTimestamp,
            )
        } else return EncryptedRecipe(
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
            categories = if (!categories.isNullOrEmpty()) categories else arrayListOf(),
            time = time,
            ingredients = ingredients as String,
            cooking = cooking as String,
            preview = preview,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp,
        )
    } catch (e: Exception) {
        throw e
    }
}

fun Recipe.toRecipeInputDto(): RecipeInputDto {
    when (this) {
        is DecryptedRecipe -> return RecipeInputDto(
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
            encrypted = isEncrypted
        )
        is EncryptedRecipe -> return RecipeInputDto(
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
            encrypted = true
        )
        else -> throw IOException()
    }
}

fun RecipeDto.info(): RecipeInfo {
    return RecipeInfo(
        remoteId = id,
        name = name,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite = isFavourite,
        isLiked = isLiked,
        servings = servings,
        calories = calories,
        isEncrypted = encrypted,
        visibility = Visibility.getByString(visibility),
        categories = if (!categories.isNullOrEmpty()) categories else arrayListOf(),
        time = time,
        preview = preview,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
    )
}