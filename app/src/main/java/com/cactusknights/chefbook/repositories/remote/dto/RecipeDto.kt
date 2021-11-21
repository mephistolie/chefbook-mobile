package com.cactusknights.chefbook.repositories.remote.dto

import com.cactusknights.chefbook.models.*
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

data class RecipeDto (
    var id: Int? = null,
    var name: String,
    @SerializedName("owner_id") var ownerId: Int = 0,
    @SerializedName("favourite") var isFavourite : Boolean = false,
    @SerializedName("liked") var isLiked : Boolean = false,

    var servings : Int = 1,
    var time : Int = 15,
    var calories : Int = 0,
    var categories : ArrayList<Int> = arrayListOf(),

    var ingredients : JsonElement,
    var cooking : JsonElement,

    var visibility : String = "private",
    @SerializedName("creation_timestamp") var creationTimestamp: Date = Date(),
    @SerializedName("update_timestamp") var updateTimestamp: Date = Date()
)

data class RecipeFavouriteInputDto (
    @SerializedName("recipe_id") var recipeId: Int,
    @SerializedName("favourite") var isFavourite : Boolean = true,
)

data class RecipeInputDto (
    var name: String,
    @SerializedName("owner_id") var ownerId: Int = 0,

    var servings : Int = 1,
    var time : Int = 15,
    var calories : Int = 0,
    var categories : ArrayList<Int> = arrayListOf(),

    var ingredients : JsonElement,
    var cooking : JsonElement,

    var visibility : String = "private"
)

fun RecipeDto.toRecipe(): DecryptedRecipe {
    val type: Type = object : TypeToken<ArrayList<MarkdownString>>() {}.type
    return DecryptedRecipe(
        remoteId = id,
        name = name,
        // TODO: ADD IS OWNED MECHANISM
        isOwned = false,
        isFavourite = isFavourite,
        isLiked = isLiked,
        servings = servings,
        calories = calories,
        visibility = Visibility.getByString(visibility),
        categories = if (categories != null) categories else arrayListOf(),
        time = time,
        ingredients = Gson().fromJson(ingredients, type),
        cooking = Gson().fromJson(cooking, type)
    )
}

fun Recipe.toRecipeInputDto(): RecipeInputDto {
    return RecipeInputDto(
        name = name,
        servings = servings,
        calories = calories,
        time = time,
        categories = categories,
        visibility = visibility.name.lowercase(),
        ingredients = Gson().toJsonTree(getIngredientsList()),
        cooking = Gson().toJsonTree(getCookingSteps())
    )
}