package com.cactusknights.chefbook.data.dto.remote.recipe

import com.cactusknights.chefbook.domain.entities.recipe.macronutrients.MacronutrientsInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MacronutrientsInfoBody(
    @SerialName("protein")
    val protein: Int? = null,
    @SerialName("fats")
    val fats: Int? = null,
    @SerialName("carbohydrates")
    val carbohydrates: Int? = null,
)

fun MacronutrientsInfoBody.toEntity(): MacronutrientsInfo =
    MacronutrientsInfo(
        protein = protein,
        fats = fats,
        carbohydrates = carbohydrates,
    )

fun MacronutrientsInfo.toSerializable(): MacronutrientsInfoBody =
    MacronutrientsInfoBody(
        protein = protein,
        fats = fats,
        carbohydrates = carbohydrates,
    )