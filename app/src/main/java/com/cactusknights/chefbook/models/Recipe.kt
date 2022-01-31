package com.cactusknights.chefbook.models

import android.util.Base64
import java.io.Serializable
import java.util.*

enum class Visibility : Serializable {
    PRIVATE, SHARED, PUBLIC;

    companion object {
        fun getByString(input: String): Visibility {
            return when (input.lowercase()) {
                "shared" -> SHARED
                "public" -> PUBLIC
                else -> PRIVATE
            }
        }
    }
}

abstract class Recipe(

    var id: Int? = null,
    var remoteId: Int? = null,
    var name: String = "",
    var likes: Int = 0,
    var ownerId: Int = 0,
    var ownerName: String = "",
    var isOwned: Boolean = false,
    var isFavourite: Boolean = false,
    var isLiked: Boolean = false,

    var description: String? = null,
    var servings: Int = 0,
    var time: Int = 0,
    var calories: Int = 0,
    var categories: ArrayList<Int> = arrayListOf(),

    var visibility: Visibility = Visibility.PRIVATE,
    var preview: String? = null,

    var creationTimestamp: Date = Date(),
    var updateTimestamp: Date = Date(),

    var encrypted: Boolean = false,
): Serializable

class DecryptedRecipe(
    id: Int? = null,
    remoteId: Int? = null,
    name: String = "",
    likes: Int = 0,
    ownerId: Int = 0,
    ownerName: String = "",
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    description: String? = null,
    servings: Int = 0,
    time: Int = 0,
    calories: Int = 0,
    categories: ArrayList<Int> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    preview: String? = null,
    var ingredients: ArrayList<Ingredient> = arrayListOf(),
    var cooking: ArrayList<CookingStep> = arrayListOf(),
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
    encrypted: Boolean = false,
): Recipe(id, remoteId, name, likes, ownerId, ownerName, isOwned, isFavourite, isLiked, description, servings, time, calories,
    categories, visibility, preview, creationTimestamp, updateTimestamp, encrypted)

class EncryptedRecipe(
    id: Int? = null,
    remoteId: Int? = null,
    name: String,
    likes: Int = 0,
    ownerId: Int = 0,
    ownerName: String = "",
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    description: String? = null,
    servings: Int = 0,
    time: Int = 0,
    calories: Int = 0,
    categories: ArrayList<Int> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    preview: String? = null,
    var ingredients: String,
    var cooking: String,
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): Recipe(id, remoteId, name, likes, ownerId, ownerName, isOwned, isFavourite, isLiked, description, servings, time, calories,
    categories, visibility, preview, creationTimestamp, updateTimestamp, true)

fun DecryptedRecipe.encrypt(encrypt: (ByteArray) -> ByteArray): EncryptedRecipe {
    val encryptedName = Base64.encodeToString(encrypt(name.toByteArray()), Base64.DEFAULT)

    val currentDescription = description
    val encryptedDescription = if (!currentDescription.isNullOrEmpty()) Base64.encodeToString(encrypt(currentDescription.toByteArray()), Base64.DEFAULT) else currentDescription

    val encryptedIngredients = Base64.encodeToString(encrypt(ingredients.toJson().toByteArray()), Base64.DEFAULT)
    val encryptedCooking = Base64.encodeToString(encrypt(cooking.toJson().toByteArray()), Base64.DEFAULT)

    val correctVisibility = if (visibility == Visibility.PUBLIC) Visibility.SHARED else visibility

    return EncryptedRecipe(
        id = id,
        remoteId = id,
        name = encryptedName,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite  = isFavourite,
        isLiked = isLiked,

        description = encryptedDescription,
        servings = servings,
        time = time,
        calories = calories,
        categories = categories,

        visibility = correctVisibility,
        preview = preview,

        ingredients = encryptedIngredients,
        cooking = encryptedCooking,

        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
    )
}

fun EncryptedRecipe.decrypt(decrypt: (ByteArray) -> ByteArray): DecryptedRecipe {
    val decryptedName = String(decrypt(Base64.decode(name, Base64.DEFAULT)))

    val decryptedDescription = if (!description.isNullOrEmpty()) String(decrypt(Base64.decode(description, Base64.DEFAULT))) else description

    val decryptedIngredients = String(decrypt(Base64.decode(ingredients, Base64.DEFAULT))).toIngredients()
    val decryptedCooking = String(decrypt(Base64.decode(cooking, Base64.DEFAULT))).toCooking()

    val correctVisibility = if (visibility == Visibility.PUBLIC) Visibility.SHARED else visibility

    return DecryptedRecipe(
        id = id,
        remoteId = id,
        name = decryptedName,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite  = isFavourite,
        isLiked = isLiked,

        description = decryptedDescription,
        servings = servings,
        time = time,
        calories = calories,
        categories = categories,

        visibility = correctVisibility,
        preview = preview,

        ingredients = decryptedIngredients,
        cooking = decryptedCooking,

        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,

        encrypted = true,
    )
}