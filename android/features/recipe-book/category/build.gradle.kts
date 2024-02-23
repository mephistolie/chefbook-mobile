plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.category"
composeDestinationsModuleName("recipebook-category")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.recipe.book.api.external)
}
