plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.dashboard"
composeDestinationsModuleName("recipebook-dashboard")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.profile.api.external)
  implementation(projects.common.sdk.recipe.book.api.external)
  implementation(projects.common.sdk.encryption.vault.api.external)

  implementation(libs.coil.compose)
}
