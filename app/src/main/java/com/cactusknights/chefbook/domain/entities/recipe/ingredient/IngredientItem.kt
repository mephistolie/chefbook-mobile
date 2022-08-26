package com.cactusknights.chefbook.domain.entities.recipe.ingredient

import com.cactusknights.chefbook.domain.entities.common.MeasureUnit

sealed class IngredientItem {

    data class Ingredient(
        val name: String,
        val amount: Int? = null,
        val unit: MeasureUnit? = null,
        val link: String? = null,
    ) : IngredientItem()

    data class Section(
        val name: String,
    ) : IngredientItem()

    data class EncryptedData(
        val content: String
    ) : IngredientItem()
}
