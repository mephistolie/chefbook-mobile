package com.cactusknights.chefbook.models

import android.util.Base64
import com.cactusknights.chefbook.common.RoomConverters
import com.cactusknights.chefbook.repositories.sync.SyncEncryptionRepository.Companion.RSA
import java.io.Serializable
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*
import javax.crypto.Cipher

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
    var ingredients: ArrayList<MarkdownString> = arrayListOf(),
    var cooking: ArrayList<MarkdownString> = arrayListOf(),
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): Recipe(id, remoteId, name, likes, ownerId, ownerName, isOwned, isFavourite, isLiked, description, servings, time, calories,
    categories, visibility, preview, creationTimestamp, updateTimestamp)

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
    categories, visibility, preview, creationTimestamp, updateTimestamp)

fun DecryptedRecipe.encrypt(publicKey: PublicKey): EncryptedRecipe {
    val cipher: Cipher = Cipher.getInstance(RSA)
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

    val encryptedName = Base64.encodeToString(cipher.doFinal(name.toByteArray()), Base64.DEFAULT)
    val currentDescription = description
    val encryptedDescription = if (!currentDescription.isNullOrEmpty()) Base64.encodeToString(cipher.doFinal(currentDescription.toByteArray()), Base64.DEFAULT) else currentDescription
    val encryptedIngredients = Base64.encodeToString(cipher.doFinal(RoomConverters.fromMarkdownString(ingredients).toByteArray()), Base64.DEFAULT)
    val encryptedCooking = Base64.encodeToString(cipher.doFinal(RoomConverters.fromMarkdownString(cooking).toByteArray()), Base64.DEFAULT)

    val correctVisibility = if (visibility == Visibility.PUBLIC) Visibility.SHARED else visibility

    return EncryptedRecipe(
        id = this.id,
        remoteId = this.id,
        name = encryptedName,
        likes = likes,
        ownerId = ownerId,
        ownerName = ownerName,
        isOwned = this.isOwned,
        isFavourite  = this.isFavourite,
        isLiked = this.isLiked,

        description = encryptedDescription,
        servings = this.servings,
        time = this.time,
        calories = this.calories,
        categories = this.categories,

        visibility = correctVisibility,
        preview = preview,

        ingredients = encryptedIngredients,
        cooking = encryptedCooking,

        creationTimestamp = this.creationTimestamp,
        updateTimestamp = this.updateTimestamp,
    )
}

fun EncryptedRecipe.decrypt(privateKey: PrivateKey): DecryptedRecipe {
    val cipher: Cipher = Cipher.getInstance(RSA)
    cipher.init(Cipher.DECRYPT_MODE, privateKey)

    val decryptedName = String(cipher.doFinal(Base64.decode(name, Base64.DEFAULT)))
    val decryptedDescription = if (!description.isNullOrEmpty()) String(cipher.doFinal(Base64.decode(description, Base64.DEFAULT))) else description
    val decryptedIngredients = RoomConverters.toMarkdownString(String(cipher.doFinal(Base64.decode(ingredients, Base64.DEFAULT))))
    val decryptedCooking = RoomConverters.toMarkdownString(String(cipher.doFinal(Base64.decode(cooking, Base64.DEFAULT))))

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

        creationTimestamp = this.creationTimestamp,
        updateTimestamp = this.updateTimestamp,
    )
}