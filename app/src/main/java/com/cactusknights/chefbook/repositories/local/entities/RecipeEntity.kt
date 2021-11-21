package com.cactusknights.chefbook.repositories.local.entities

import androidx.room.*
import com.cactusknights.chefbook.legacy.migration.TABLE_NAME
import com.cactusknights.chefbook.models.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["remoteId"], unique = true)])
@TypeConverters(RoomConverters::class)
data class RecipeEntity constructor(

    @PrimaryKey(autoGenerate = true) @SerializedName("recipe_id")
    var id: Int,

    @SerializedName("remote_id")
    var remoteId: Int? = null,

    var name: String,

    @SerializedName("owned")
    var isOwned: Boolean,

    @SerializedName("favourite")
    var isFavourite: Boolean = false,

    @SerializedName("liked")
    var isLiked: Boolean = false,

    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,

    var ingredients: String,
    var cooking: String,

    @SerializedName("visibility")
    var visibility: Visibility,

    @SerializedName("encrypted")
    var isEncrypted: Boolean,

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
            id = id,
            name = name,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            servings = servings,
            time = time,
            calories = calories,
            ingredients = ingredients,
            cooking = cooking,
            visibility = visibility,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp
        )
    else
        DecryptedRecipe(
            id = id,
            name = name,
            isOwned = isOwned,
            isFavourite = isFavourite,
            isLiked = isLiked,
            servings = servings,
            time = time,
            calories = calories,
            ingredients = RoomConverters.toMarkdownString(ingredients),
            cooking = RoomConverters.toMarkdownString(cooking),
            visibility = visibility,
            creationTimestamp = creationTimestamp,
            updateTimestamp = updateTimestamp
        )
}

fun Recipe.toRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        remoteId = remoteId,
        name = name,
        isOwned = isOwned,
        isFavourite = isFavourite,
        isLiked = isLiked,
        servings = servings,
        time = time,
        calories = calories,
        ingredients = Gson().toJson(getCookingSteps()),
        cooking = Gson().toJson(getCookingSteps()),
        isEncrypted = this is EncryptedRecipe,
        visibility = visibility,
        creationTimestamp = creationTimestamp,
        updateTimestamp = updateTimestamp
    )
}