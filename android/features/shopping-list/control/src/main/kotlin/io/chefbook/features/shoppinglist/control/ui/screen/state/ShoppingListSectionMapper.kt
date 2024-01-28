package io.chefbook.features.shoppinglist.control.ui.screen.state

import io.chefbook.sdk.shoppinglist.api.external.domain.entities.ShoppingList

internal object ShoppingListSectionMapper {

  fun map(shoppingList: ShoppingList): List<ShoppingListSection> =
    shoppingList.purchases
      .filter { it.name.isNotEmpty() }
      .groupBy { purchase -> purchase.recipeId }
      .map { section ->
        ShoppingListSection(
          purchases = section.value,
          recipeId = section.key,
          title = shoppingList.recipeNames[section.key],
        )
      }
      .sortedWith(compareBy(
        { it.title == null },
        { it.title?.uppercase() },
        { it.recipeId }
      ))
}
