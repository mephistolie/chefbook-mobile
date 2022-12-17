package com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation

import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList

object ShoppingListSectionMapper {

    fun map(shoppingList: ShoppingList): List<ShoppingListSection> =
        shoppingList.purchases
            .groupBy { purchase -> purchase.recipeId }
            .map { section ->
                ShoppingListSection(
                    purchases = section.value,
                    recipeId = section.key,
                    title = section.value.firstOrNull { it.recipeName != null }?.recipeName
                )
            }
            .sortedWith(compareBy({ it.title?.uppercase() }, { it.recipeId }))

}
