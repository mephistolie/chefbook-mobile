import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipebook.category"
composeDestinationsModuleName("recipebook-category")

dependencies {
  implementation(projects.android.features.recipeBook.core)

  implementation(projects.common.sdk.recipe.book.api.external)
  implementation(projects.common.sdk.tag.api.external)
}
