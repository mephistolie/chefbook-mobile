package com.cactusknights.chefbook.models

import com.google.type.DateTime
import java.io.Serializable
import java.util.*
import kotlin.collections.List

enum class Visibility {
    PRIVATE, SHARED, PUBLIC
}

abstract class BaseRecipe(

    var id: Int = 1,
    var remoteId: Int = id,
    var name: String = "Recipe",
    var isOwned: Boolean = false,
    var isFavourite: Boolean = false,
    var isLiked: Boolean = false,

    var servings: Int = 1,
    var time: Int = 15,
    var calories: Int = 0,
    var categories: ArrayList<String> = arrayListOf(),

    var visibility: Visibility = Visibility.PRIVATE,

    var creationTimestamp: Date = Date(),
    var updateTimestamp: Date = Date(),
): Serializable {
    abstract fun getIngredientsList(key: String = ""): ArrayList<Selectable<String>>
    abstract fun setIngredientsList(data: ArrayList<Selectable<String>>)
    abstract fun getCookingSteps(key: String = ""): ArrayList<Selectable<String>>
    abstract fun setCookingSteps(data: ArrayList<Selectable<String>>)
}

class Recipe(
    id: Int = 1,
    remoteId: Int = id,
    name: String = "Recipe",
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    servings: Int = 1,
    time: Int = 15,
    calories: Int = 0,
    categories: ArrayList<String> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    var ingredients: ArrayList<Selectable<String>> = arrayListOf(),
    var cooking: ArrayList<Selectable<String>> = arrayListOf(),
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): BaseRecipe(id, remoteId, name, isOwned, isFavourite, isLiked, servings, time, calories,
    categories, visibility, creationTimestamp, updateTimestamp) {
    override fun getIngredientsList(key: String): ArrayList<Selectable<String>> {
        return ingredients
    }

    override fun setIngredientsList(data: ArrayList<Selectable<String>>) {
        ingredients = data
    }

    override fun getCookingSteps(key: String): ArrayList<Selectable<String>> {
        return cooking
    }

    override fun setCookingSteps(data: ArrayList<Selectable<String>>) {
        cooking = data
    }

}

class EncryptedRecipe(
    id: Int = 1,
    remoteId: Int = id,
    name: String,
    isOwned: Boolean = false,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,

    servings: Int = 1,
    time: Int = 15,
    calories: Int = 0,
    categories: ArrayList<String> = arrayListOf(),
    visibility: Visibility = Visibility.PRIVATE,
    var ingredients: String,
    var cooking: String,
    creationTimestamp: Date = Date(),
    updateTimestamp: Date = Date(),
): BaseRecipe(id, remoteId, name, isOwned, isFavourite, isLiked, servings, time, calories,
    categories, visibility, creationTimestamp, updateTimestamp) {
    override fun getIngredientsList(key: String): ArrayList<Selectable<String>> {
        // TODO("Add encrypt")
        return arrayListOf()
    }

    override fun setIngredientsList(data: ArrayList<Selectable<String>>) {
        // TODO("Add encrypt")
        ingredients = "asd"
    }

    override fun getCookingSteps(key: String): ArrayList<Selectable<String>> {
        // TODO("Add encrypt")
        return arrayListOf()
    }

    override fun setCookingSteps(data: ArrayList<Selectable<String>>) {
        // TODO("Add encrypt")
        cooking = "asd"
    }

}