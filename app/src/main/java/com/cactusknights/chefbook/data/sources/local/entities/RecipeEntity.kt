package com.cactusknights.chefbook.data.sources.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cactusknights.chefbook.core.Converters
import com.cactusknights.chefbook.core.room.RoomConverters
import com.cactusknights.chefbook.models.*
import com.cactusknights.chefbook.data.sources.local.entities.RecipeEntity.Companion.TABLE_NAME
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

    @SerializedName("user_timestamp")
    var userTimestamp: Date,
) {

    companion object {
        const val TABLE_NAME = "recipes"
    }
}

fun RecipeEntity.toRecipe(): Recipe {
    return if (isEncrypted)
        EncryptedRecipe(
            id = id,
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
            isEncrypted = true,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp,
            userTimestamp = userTimestamp,
        )
    else
        DecryptedRecipe(
            id = id,
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
            ingredients = Converters.jsonToIngredients(ingredients),
            cooking = Converters.jsonToCooking(cooking),
            visibility = visibility,
            preview = preview,
            isEncrypted = isEncrypted,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp,
            userTimestamp = userTimestamp,
        )
}

fun Recipe.toRecipeEntity(): RecipeEntity {
    val recipeCopy = this
    val ingredients =
        if (recipeCopy is DecryptedRecipe) Converters.ingredientsToJson(recipeCopy.ingredients) else (recipeCopy as EncryptedRecipe).ingredients
    val cooking =
        if (recipeCopy is DecryptedRecipe) Converters.cookingToJson(recipeCopy.cooking) else (recipeCopy as EncryptedRecipe).cooking
    return RecipeEntity(
        id = id,
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
        isEncrypted = isEncrypted,
        visibility = visibility,
        preview = preview,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        userTimestamp = userTimestamp,
    )
}

fun RecipeEntity.info(): RecipeInfo {
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
        calories = calories,
        isEncrypted = isEncrypted,
        visibility = visibility,
        time = time,
        preview = preview,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp,
        userTimestamp = userTimestamp,
    )
}