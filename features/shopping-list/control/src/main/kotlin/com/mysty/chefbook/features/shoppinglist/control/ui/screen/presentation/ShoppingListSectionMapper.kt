package com.mysty.chefbook.features.shoppinglist.control.ui.screen.presentation

import com.mysty.chefbook.api.shoppinglist.domain.entities.ShoppingList

internal object ShoppingListSectionMapper {

  fun map(shoppingList: ShoppingList): List<ShoppingListSection> =
    shoppingList.purchases
      .filter { it.name.isNotEmpty() }
      .groupBy { purchase -> purchase.recipeId }
      .map { section ->
        ShoppingListSection(
          purchases = section.value,
          recipeId = section.key,
          title = section.value.firstOrNull { it.recipeName != null }?.recipeName
        )
      }
      .sortedWith(compareBy(
        { it.title == null },
        { it.title?.uppercase() },
        { it.recipeId }
      ))
}
