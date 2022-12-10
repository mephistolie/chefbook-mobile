package com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation

import com.cactusknights.chefbook.domain.entities.shoppinglist.ShoppingList

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

}
