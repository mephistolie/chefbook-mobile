package com.mysty.chefbook.api.recipe.data.remote.dto

import androidx.annotation.Keep
import com.mysty.chefbook.api.recipe.domain.entities.macronutrients.MacronutrientsInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MacronutrientsInfoBody(
    @SerialName("protein")
    val protein: Int? = null,
    @SerialName("fats")
    val fats: Int? = null,
    @SerialName("carbohydrates")
    val carbohydrates: Int? = null,
) {
    fun toEntity(): MacronutrientsInfo =
        MacronutrientsInfo(
            protein = protein,
            fats = fats,
            carbohydrates = carbohydrates,
        )
}

internal fun MacronutrientsInfo.toSerializable(): MacronutrientsInfoBody =
    MacronutrientsInfoBody(
        protein = protein,
        fats = fats,
        carbohydrates = carbohydrates,
    )
