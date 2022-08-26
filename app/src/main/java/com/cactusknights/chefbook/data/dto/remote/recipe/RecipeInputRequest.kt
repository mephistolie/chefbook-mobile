package com.cactusknights.chefbook.data.dto.remote.recipe

import com.cactusknights.chefbook.core.mappers.LanguageMapper
import com.cactusknights.chefbook.core.mappers.VisibilityMapper
import com.cactusknights.chefbook.data.dto.common.recipe.CookingItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable
import com.cactusknights.chefbook.data.dto.common.recipe.toSerializable
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeInputRequest(
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

fun RecipeInput.toSerializable() =
    RecipeInputRequest(
        name = name,
        visibility = VisibilityMapper.map(visibility),
        isEncrypted = isEncrypted,
        language = LanguageMapper.map(language),
        description = description,
        preview = preview,

        servings = servings,
        time = time,

        calories = calories,
        macronutrients = macronutrients?.toSerializable(),

        ingredients = ingredients.map { it.toSerializable() },
        cooking = cooking.map { it.toSerializable() },
    )