plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.search"
composeDestinationsModuleName("recipebook-search")

dependencies {
  implementation(projects.common.sdk.recipe.book.api.external)
}
