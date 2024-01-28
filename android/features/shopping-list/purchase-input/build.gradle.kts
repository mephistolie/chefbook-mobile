plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.shoppinglist.purchases.input"
composeDestinationsModuleName("shoppinglist-purchaseinput")

dependencies {
  implementation(projects.common.libs.models)
  implementation(projects.common.sdk.shoppingList.api.external)
}
