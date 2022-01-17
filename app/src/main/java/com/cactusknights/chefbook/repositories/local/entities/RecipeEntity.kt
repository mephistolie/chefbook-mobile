package com.cactusknights.chefbook.repositories.local.entities

import androidx.room.*
import com.cactusknights.chefbook.common.RoomConverters
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.repositories.local.entities.RecipeEntity.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["remoteId"], unique = true)])
@TypeConverters(RoomConverters::class)
data class RecipeEntity constructor(

    @PrimaryKey(autoGenerate = true) @SerializedName("recipe_id")
    var id: Int? = null,

    @SerializedName("remote_id")
    var remoteId: Int? = null,

    var name: String,

    @SerializedName("likes")
    var likes: Int = 0,

    @SerializedName("owner_id")
    var ownerId: Int = 0,
    @SerializedName("owner_name")
    var ownerName: String = "",
    @SerializedName("owned")
    var isOwned: Boolean,

    @SerializedName("favourite")
    var isFavourite: Boolean = false,

    @SerializedName("liked")
    var isLiked: Boolean = false,

    @SerializedName("description")
    var description: String? = null,

    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,

    var ingredients: String,
    var cooking: String,

    @SerializedName("visibility")
    var visibility: Visibility,

    @SerializedName("encrypted")
    var isEncrypted: Boolean,

    @SerializedName("preview")
    var preview: String? = null,

    @SerializedName("creation_timestamp")
    var creationTimestamp: Date,

    @SerializedName("update_timestamp")
    var updateTimestamp: Date,
) {

    companion object {
        const val TABLE_NAME = "recipes"
    }
}

fun RecipeEntity.toRecipe(): Recipe {
    return if (isEncrypted)
        EncryptedRecipe(
            id = id!!.or(0),
            remoteId = remoteId,
            name = name,
            likes = likes,
            ownerId = ownerId,
            ownerName = ownerName,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            description = description,
            servings = servings,
            time = time,
            calories = calories,
            ingredients = ingredients,
            cooking = cooking,
            visibility = visibility,
            preview = preview,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp
        )
    else
        DecryptedRecipe(
            id = id!!.or(0),
            remoteId = remoteId,
            name = name,
            likes = likes,
            ownerId = ownerId,
            ownerName = ownerName,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            description = description,
            servings = servings,
            time = time,
            calories = calories,
            ingredients = RoomConverters.toMarkdownString(ingredients),
            cooking = RoomConverters.toMarkdownString(cooking),
            visibility = visibility,
            preview = preview,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp
        )
}

fun Recipe.toRecipeEntity(): RecipeEntity {
    val recipeCopy = this
    val ingredients = if (recipeCopy is DecryptedRecipe)  RoomConverters.fromMarkdownString(recipeCopy.ingredients) else (recipeCopy as EncryptedRecipe).ingredients
    val cooking = if (recipeCopy is DecryptedRecipe)  RoomConverters.fromMarkdownString(recipeCopy.cooking) else (recipeCopy as EncryptedRecipe).cooking
    return RecipeEntity(
        id = id,
        remoteId = remoteId,
        name = name,
        likes = likes,
        isOwned = isOwned,
        isFavourite = isFavourite,
        isLiked = isLiked,
        description = description,
        servings = servings,
        time = time,
        calories = calories,
        ingredients = ingredients,
        cooking = cooking,
        isEncrypted = this is EncryptedRecipe,
        visibility = visibility,
        preview = preview,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp
    )
}