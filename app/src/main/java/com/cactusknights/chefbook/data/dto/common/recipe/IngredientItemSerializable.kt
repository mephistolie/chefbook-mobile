package com.cactusknights.chefbook.data.dto.common.recipe

import com.cactusknights.chefbook.core.mappers.MeasureUnitMapper
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable.Companion.TYPE_ENCRYPTED_DATA
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable.Companion.TYPE_INGREDIENT
import com.cactusknights.chefbook.data.dto.common.recipe.IngredientItemSerializable.Companion.TYPE_SECTION
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientItemSerializable(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("type")
    val type: String,
    @SerialName("amount")
    val amount: Int? = null,
    @SerialName("unit")
    val unit: String? = null,
    @SerialName("link")
    val link: String? = null,
) {
    companion object {
        const val TYPE_INGREDIENT = "ingredient"
        const val TYPE_SECTION = "section"
        const val TYPE_ENCRYPTED_DATA = "encrypted_data"
    }
}

fun IngredientItemSerializable.toEntity(): IngredientItem = when (this.type.lowercase()) {
    TYPE_SECTION -> IngredientItem.Section(
        id = id,
        name = text,
    )
    TYPE_ENCRYPTED_DATA -> IngredientItem.EncryptedData(
        id = id,
        content = text
    )
    else -> IngredientItem.Ingredient(
        id = id,
        name = text,
        amount = amount,
        unit = MeasureUnitMapper.map(unit),
        link = link,
    )
}

fun IngredientItem.toSerializable(): IngredientItemSerializable = when (this) {
    is IngredientItem.Ingredient -> IngredientItemSerializable(
        id = id,
        text = name,
        amount = amount,
        unit = MeasureUnitMapper.map(unit),
        link = link,
        type = TYPE_INGREDIENT,
    )
    is IngredientItem.Section -> IngredientItemSerializable(
        id = id,
        text = name,
        type = TYPE_SECTION,
    )
    is IngredientItem.EncryptedData ->
        IngredientItemSerializable(
            id = id,
            text = content,
            type = TYPE_ENCRYPTED_DATA,
        )
}