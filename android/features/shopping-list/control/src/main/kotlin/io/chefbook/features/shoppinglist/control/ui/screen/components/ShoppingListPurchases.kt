package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.features.shoppinglist.control.ui.screen.state.ShoppingListSection

internal fun LazyListScope.shoppingListPurchases(
  shoppingList: List<ShoppingListSection>,
  onTitleClick: (String) -> Unit,
  onPurchaseClick: (String) -> Unit,
  onEditPurchaseClick: (String) -> Unit,
) {
  item { Spacer(modifier = Modifier.height(shoppingListActionBarHeight + 8.dp)) }
  itemsIndexed(
    items = shoppingList,
    key = { _, section -> section.recipeId ?: section.title.orEmpty() }
  ) { index, section ->
    ShoppingListSection(
      state = section,
      onPurchaseClick = onPurchaseClick,
      onEditPurchaseClick = onEditPurchaseClick,
      onRecipeOpen = onTitleClick,
      modifier = Modifier.padding(bottom = if (index < shoppingList.lastIndex) 6.dp else 0.dp)
    )
  }
  item { Spacer(modifier = Modifier.height(shoppingListActionBarHeight + 8.dp)) }
}

