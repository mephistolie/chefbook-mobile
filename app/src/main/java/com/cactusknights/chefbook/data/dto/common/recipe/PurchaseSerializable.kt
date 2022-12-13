package com.cactusknights.chefbook.data.dto.common.recipe

import com.cactusknights.chefbook.core.mappers.MeasureUnitMapper
import com.cactusknights.chefbook.domain.entities.shoppinglist.Purchase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseSerializable(
    @SerialName("purchase_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("amount")
    val amount: Int? = null,
    @SerialName("unit")
    val unit: String? = null,
    @SerialName("multiplier")
    val multiplier: Int? = null,
    @SerialName("is_purchased")
    val isPurchased: Boolean? = null,
    @SerialName("recipe_id")
    val recipeId: String? = null,
    @SerialName("recipe_name")
    val recipeName: String? = null,
)

fun PurchaseSerializable.toEntity() = Purchase(
    id = id,
    name = name,
    amount = amount,
    unit = MeasureUnitMapper.map(unit),
    multiplier = multiplier ?: 1,
    isPurchased = isPurchased ?: false,
    recipeId = recipeId,
    recipeName = recipeName,
)

fun Purchase.toSerializable() = PurchaseSerializable(
    id = id,
    name = name,
    amount = amount,
    unit = MeasureUnitMapper.map(unit),
    multiplier = multiplier,
    isPurchased = isPurchased,
    recipeId = recipeId,
    recipeName = recipeName,
)
