import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipebook.categories"
composeDestinationsModuleName("recipebook-categories")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.recipe.book.api.external)
  implementation(projects.common.sdk.tag.api.external)
}
