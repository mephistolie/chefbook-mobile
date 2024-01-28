plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.shoppinglist.control"
composeDestinationsModuleName("shoppinglist-control")

dependencies {
  implementation(projects.common.sdk.shoppingList.api.external)

  implementation(projects.android.features.shoppingList.purchaseInput)
}
