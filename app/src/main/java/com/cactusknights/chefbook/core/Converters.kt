package com.cactusknights.chefbook.core

import com.cactusknights.chefbook.models.CookingStep
import com.cactusknights.chefbook.models.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList
import javax.inject.Inject

object Converters {

   private val gson = Gson()

    fun ingredientsToJson(data: ArrayList<Ingredient>): String {
        return gson.toJson(data)
    }

    fun jsonToIngredients(data: String): ArrayList<Ingredient> {
        val type: Type = object : TypeToken<ArrayList<Ingredient>>() {}.type
        return gson.fromJson(data, type)
    }

    fun cookingToJson(data: ArrayList<CookingStep>): String {
        return gson.toJson(data)
    }

    fun jsonToCooking(data: String): ArrayList<CookingStep> {
        val type: Type = object : TypeToken<ArrayList<CookingStep>>() {}.type
        return gson.fromJson(data, type)
    }
}