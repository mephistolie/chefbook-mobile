package com.cactusknights.chefbook.models

import com.cactusknights.chefbook.DataSources
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

    var id: Int = -1,
    var remoteId: Int? = null,
    var name: String = "",
    var isOwned: Boolean = false,
    var isFavourite: Boolean = false,
    var isLiked: Boolean = false,

    var servings: Int = 0,
    var time: Int = 0,
    var calories: Int = 0,
    var categories: ArrayList<Int> = arrayListOf(),

    var visibility: Visibility = Visibility.PRIVATE,

    var creationTimestamp: Date = Date(),
    var updateTimestamp: Date = Date(),
): Serializable {
    abstract fun getIngredientsList(key: String = ""): ArrayList<MarkdownString>
    abstract fun setIngredientsList(data: ArrayList<MarkdownString>)
    abstract fun getCookingSteps(key: String = ""): ArrayList<MarkdownString>
    abstract fun setCookingSteps(data: ArrayList<MarkdownString>)
}

class DecryptedRecipe(
    id: Int = -1,
    remoteId: Int? = null,
    name: String = "",
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    servings: Int = 0,
    time: Int = 0,
    calories: Int = 0,
    categories: ArrayList<Int> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    var ingredients: ArrayList<MarkdownString> = arrayListOf(),
    var cooking: ArrayList<MarkdownString> = arrayListOf(),
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): Recipe(id, remoteId, name, isOwned, isFavourite, isLiked, servings, time, calories,
    categories, visibility, creationTimestamp, updateTimestamp) {
    override fun getIngredientsList(key: String): ArrayList<MarkdownString> {
        return ingredients
    }

    override fun setIngredientsList(data: ArrayList<MarkdownString>) {
        ingredients = data
    }

    override fun getCookingSteps(key: String): ArrayList<MarkdownString> {
        return cooking
    }

    override fun setCookingSteps(data: ArrayList<MarkdownString>) {
        cooking = data
    }

}

class EncryptedRecipe(
    id: Int = -1,
    remoteId: Int? = null,
    name: String,
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    servings: Int = 0,
    time: Int = 0,
    calories: Int = 0,
    categories: ArrayList<Int> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    var ingredients: String,
    var cooking: String,
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): Recipe(id, remoteId, name, isOwned, isFavourite, isLiked, servings, time, calories,
    categories, visibility, creationTimestamp, updateTimestamp) {
    override fun getIngredientsList(key: String): ArrayList<MarkdownString> {
        // TODO("Add encrypt")
        return arrayListOf()
    }

    override fun setIngredientsList(data: ArrayList<MarkdownString>) {
        // TODO("Add encrypt")
        ingredients = "asd"
    }

    override fun getCookingSteps(key: String): ArrayList<MarkdownString> {
        // TODO("Add encrypt")
        return arrayListOf()
    }

    override fun setCookingSteps(data: ArrayList<MarkdownString>) {
        // TODO("Add encrypt")
        cooking = "asd"
    }

}

fun DecryptedRecipe.encrypt(password: String): EncryptedRecipe {
    return EncryptedRecipe(
        id = this.id,
        remoteId = this.id,
        name = this.name,
        isOwned = this.isOwned,
        isFavourite  = this.isFavourite,
        isLiked = this.isLiked,

        servings = this.servings,
        time = this.time,
        calories = this.calories,
        categories = this.categories,
        visibility = this.visibility,

        ingredients = "",
        cooking = "",

        creationTimestamp = this.creationTimestamp,
        updateTimestamp = this.updateTimestamp,
    )
}