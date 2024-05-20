plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.search"
composeDestinationsModuleName("recipebook-search")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.profile.api.external)
  implementation(projects.common.sdk.recipe.book.api.external)
}
