plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipe.info"
composeDestinationsModuleName("recipe-info")

dependencies {
  implementation(projects.common.libs.models)
  implementation(projects.common.libs.utils)
  implementation(projects.common.sdk.settings.api.external)
  implementation(projects.common.sdk.recipe.crud.api.external)
  implementation(projects.common.sdk.recipe.interaction.api.external)
  implementation(projects.common.sdk.shoppingList.api.external)

  implementation(projects.android.features.recipe.control)
  implementation(projects.android.features.recipe.rating)

  implementation(libs.coil.compose)

  coreLibraryDesugaring(libs.jdk.desugar)
}
