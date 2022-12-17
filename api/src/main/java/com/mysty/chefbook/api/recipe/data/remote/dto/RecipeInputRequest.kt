package com.mysty.chefbook.api.recipe.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.recipe.data.common.dto.CookingItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.IngredientItemSerializable
import com.mysty.chefbook.api.recipe.data.common.dto.toSerializable
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecipeInputRequest(
    @SerialName("recipe_id")
    val id: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("visibility")
    val visibility: String,
    @SerialName("encrypted")
    val isEncrypted: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("preview")
    val preview: String? = null,

    @SerialName("servings")
    val servings: Int? = null,
    @SerialName("time")
    val time: Int? = null,

    @SerialName("calories")
    val calories: Int? = null,
    @SerialName("macronutrients")
    val macronutrients: MacronutrientsInfoBody? = null,

    @SerialName("ingredients")
    val ingredients: List<IngredientItemSerializable>,
    @SerialName("cooking")
    val cooking: List<CookingItemSerializable>,
)

internal fun RecipeInput.toSerializable(recipeId: String? = null) =
    RecipeInputRequest(
        name = name,
        visibility = visibility.code,
        isEncrypted = isEncrypted,
        language = language.code,
        description = description,
        preview = preview,

        servings = servings,
        time = time,

        calories = calories,
        macronutrients = macronutrients?.toSerializable(),

        ingredients = ingredients.map(IngredientItem::toSerializable),
        cooking = cooking.map(CookingItem::toSerializable),
    )