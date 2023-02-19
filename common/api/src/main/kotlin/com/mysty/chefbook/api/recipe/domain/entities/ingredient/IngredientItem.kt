package com.mysty.chefbook.api.recipe.domain.entities.ingredient

import com.mysty.chefbook.api.common.entities.unit.MeasureUnit

sealed class IngredientItem(
    open val id: String
) {

    data class Ingredient(
        override val id: String,
        val name: String,
        val amount: Int? = null,
        val unit: MeasureUnit? = null,
        val link: String? = null,
    ) : IngredientItem(id)

    data class Section(
        override val id: String,
        val name: String,
    ) : IngredientItem(id)

    data class EncryptedData(
        override val id: String,
        val content: String,
    ) : IngredientItem(id)
}
