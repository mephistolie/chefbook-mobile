package com.mysty.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.presentation.ShoppingListSection

internal fun LazyListScope.shoppingListPurchases(
  shoppingList: List<ShoppingListSection>,
  onTitleClick: (String) -> Unit,
  onPurchaseClick: (String) -> Unit,
  onEditPurchaseClick: (String) -> Unit,
) {
  itemsIndexed(
    items = shoppingList,
    key = { _, section -> section.recipeId.orEmpty() }
  ) { index, section ->
    ShoppingListSection(
      state = section,
      onPurchaseClick = onPurchaseClick,
      onEditPurchaseClick = onEditPurchaseClick,
      onRecipeOpen = onTitleClick,
      modifier = Modifier.padding(bottom = if (index < shoppingList.lastIndex) 6.dp else 0.dp)
    )
  }
}

