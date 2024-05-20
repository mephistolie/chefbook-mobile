plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.categories"
composeDestinationsModuleName("recipebook-categories")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.recipe.book.api.external)
  implementation(projects.common.sdk.tag.api.external)
}
