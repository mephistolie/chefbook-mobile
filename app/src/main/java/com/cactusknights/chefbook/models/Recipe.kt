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

abstract class Recipe : Serializable {
    abstract var id: Int?
    abstract var remoteId: Int?
    abstract var name: String
    abstract var likes: Int
    abstract var ownerId: Int
    abstract var ownerName: String
    abstract var isOwned: Boolean
    abstract var isFavourite: Boolean
    abstract var isLiked: Boolean

    abstract var description: String?
    abstract var servings: Int
    abstract var time: Int
    abstract var calories: Int
    abstract var categories: ArrayList<Int>

    abstract var visibility: Visibility
    abstract var preview: String?

    abstract var creationTimestamp: Date
    abstract var updateTimestamp: Date
    abstract var userTimestamp: Date

    abstract var isEncrypted: Boolean
}

data class DecryptedRecipe(
    override var id: Int? = null,
    override var remoteId: Int? = null,
    override var name: String = "",
    override var likes: Int = 0,
    override var ownerId: Int = 0,
    override var ownerName: String = "",
    override var isOwned: Boolean = false,
    override var isFavourite: Boolean = false,
    override var isLiked: Boolean = false,

    override var description: String? = null,
    override var servings: Int = 0,
    override var time: Int = 0,
    override var calories: Int = 0,
    override var categories: ArrayList<Int> = arrayListOf(),
    override var visibility: Visibility = Visibility.PRIVATE,
    override var preview: String? = null,
    var ingredients: ArrayList<Ingredient> = arrayListOf(),
    var cooking: ArrayList<CookingStep> = arrayListOf(),
    override var creationTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var updateTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var userTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var isEncrypted: Boolean = false,
) : Recipe()

data class EncryptedRecipe(
    override var id: Int? = null,
    override var remoteId: Int? = null,
    override var name: String,
    override var likes: Int = 0,
    override var ownerId: Int = 0,
    override var ownerName: String = "",
    override var isOwned: Boolean = false,
    override var isFavourite: Boolean = false,
    override var isLiked: Boolean = false,

    override var description: String? = null,
    override var servings: Int = 0,
    override var time: Int = 0,
    override var calories: Int = 0,
    override var categories: ArrayList<Int> = arrayListOf(),
    override var visibility: Visibility = Visibility.PRIVATE,
    override var preview: String? = null,
    var ingredients: String,
    var cooking: String,
    override var creationTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var updateTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var userTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    override var isEncrypted: Boolean = true,
) : Recipe()

fun DecryptedRecipe.encrypt(encrypt: (ByteArray) -> ByteArray): EncryptedRecipe {
    var encryptedName = Base64.encodeToString(encrypt(name.toByteArray()), Base64.DEFAULT)

    var currentDescription = description
    var encryptedDescription = if (!currentDescription.isNullOrEmpty()) Base64.encodeToString(
        encrypt(currentDescription.toByteArray()),
        Base64.DEFAULT
    ) else currentDescription

    var encryptedIngredients =
        Base64.encodeToString(encrypt(ingredients.toJson().toByteArray()), Base64.DEFAULT)
    var encryptedCooking =
        Base64.encodeToString(encrypt(cooking.toJson().toByteArray()), Base64.DEFAULT)

    var correctVisibility = if (visibility == Visibility.PUBLIC) Visibility.SHARED else visibility

    return EncryptedRecipe(
        id = id,
        remoteId = remoteId,
        name = encryptedName,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite = isFavourite,
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
        userTimestamp = userTimestamp,
    )
}

fun EncryptedRecipe.decrypt(decrypt: (ByteArray) -> ByteArray): DecryptedRecipe {
    var decryptedName = String(decrypt(Base64.decode(name, Base64.DEFAULT)))

    var decryptedDescription = if (!description.isNullOrEmpty()) String(
        decrypt(
            Base64.decode(
                description,
                Base64.DEFAULT
            )
        )
    ) else description

    var decryptedIngredients =
        String(decrypt(Base64.decode(ingredients, Base64.DEFAULT))).toIngredients()
    var decryptedCooking = String(decrypt(Base64.decode(cooking, Base64.DEFAULT))).toCooking()

    var correctVisibility = if (visibility == Visibility.PUBLIC) Visibility.SHARED else visibility

    return DecryptedRecipe(
        id = id,
        remoteId = remoteId,
        name = decryptedName,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite = isFavourite,
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
        userTimestamp = userTimestamp,

        isEncrypted = true,
    )
}

fun DecryptedRecipe.withoutPictures(): DecryptedRecipe =
    this.copy(
        preview = null,
        cooking = this.cooking.map { step ->
            step.pictures = arrayListOf(); step
        } as ArrayList<CookingStep>
    )

data class RecipeInfo(

    var id: Int? = null,
    var remoteId: Int? = null,
    var name: String = "",
    var likes: Int = 0,
    var ownerId: Int = 0,
    var ownerName: String = "",
    var isOwned: Boolean = false,
    var isFavourite: Boolean = false,
    var isLiked: Boolean = false,

    var servings: Int = 0,
    var time: Int = 0,
    var calories: Int = 0,
    var categories: List<Int> = listOf(),

    var visibility: Visibility = Visibility.PRIVATE,
    var preview: String? = null,

    var creationTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    var updateTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),
    var userTimestamp: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis),

    var isEncrypted: Boolean = false,
) : Serializable

fun Recipe.info(): RecipeInfo {
    return RecipeInfo(
        id = id,
        remoteId = remoteId,
        name = name,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = isOwned,
        isFavourite = isFavourite,
        isLiked = isLiked,

        servings = servings,
        time = time,
        calories = calories,
        categories = categories,

        visibility = visibility,
        preview = preview,

        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        userTimestamp = userTimestamp,

        isEncrypted = isEncrypted,
    )
}

fun RecipeInfo.decrypt(decrypt: (ByteArray) -> ByteArray): RecipeInfo {
    return this.copy(name = String(decrypt(Base64.decode(name, Base64.DEFAULT))))
}