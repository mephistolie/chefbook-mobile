plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.category"
composeDestinationsModuleName("recipebook-category")

dependencies {
  implementation(projects.common.sdk.recipe.book.api.external)
}
