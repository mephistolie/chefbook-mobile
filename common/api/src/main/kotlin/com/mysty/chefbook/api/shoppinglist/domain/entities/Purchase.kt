package com.mysty.chefbook.api.shoppinglist.domain.entities

import com.mysty.chefbook.api.common.entities.unit.MeasureUnit

data class Purchase(
    val id: String,
    val name: String,
    val amount: Int? = null,
    val unit: MeasureUnit? = null,
    val multiplier: Int = 1,
    val isPurchased: Boolean = false,
    val recipeId: String? = null,
    val recipeName: String? = null,
)
