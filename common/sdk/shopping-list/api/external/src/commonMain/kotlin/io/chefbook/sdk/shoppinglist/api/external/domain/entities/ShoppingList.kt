package io.chefbook.sdk.shoppinglist.api.external.domain.entities

data class ShoppingList(
  val meta: ShoppingListMeta,
  val purchases: List<Purchase>,
  val recipeNames: Map<String, String>,
) {

  val id
    get() = meta.id

  val name
    get() = meta.name

  val type
    get() = meta.type

  val owner
    get() = meta.owner

  val version
    get() = meta.version

  fun plusPurchases(
    newPurchases: List<Purchase>,
    newRecipeNames: Map<String, String>,
  ): ShoppingList {
    val purchasesById = purchases.associateBy({ it.id }, { it }).toMutableMap()
    val purchasesByName = purchases.associateBy({ it.name }, { it }).toMutableMap()

    val updatedPurchases = mutableListOf<Purchase>()

    newPurchases.forEach { newPurchase ->
      val purchaseById = purchasesById[newPurchase.id]
      val purchaseByName = purchasesByName[newPurchase.name]
      val newPurchaseAmount = newPurchase.amount
      when {
        purchaseById != null && newPurchaseAmount != null && newPurchaseAmount > 0 ->
          updatedPurchases.add(
            purchaseById.copy(amount = (purchaseById.amount ?: 0) + newPurchaseAmount)
          )

        purchaseByName != null ->
          updatedPurchases.add(
            purchaseByName.copy(multiplier = (purchaseByName.multiplier ?: 1) + 1)
          )

        else -> updatedPurchases.add(newPurchase)
      }
      purchasesById.remove(newPurchase.id)
    }

    updatedPurchases.addAll(purchasesById.values)

    return copy(
      purchases = updatedPurchases.toList(),
      recipeNames = recipeNames.plus(newRecipeNames)
    )
  }
}
