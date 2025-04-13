import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.shoppinglist.control"
composeDestinationsModuleName("shoppinglist-control")

dependencies {
  implementation(projects.common.sdk.shoppingList.api.external)

  implementation(projects.android.features.shoppingList.purchaseInput)
}
