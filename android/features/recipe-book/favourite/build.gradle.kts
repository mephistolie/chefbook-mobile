plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.favourite"
composeDestinationsModuleName("recipebook-favourite")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.recipe.book.api.external)
}
