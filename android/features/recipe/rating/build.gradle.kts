plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipe.rating"
composeDestinationsModuleName("recipe-rating")

dependencies {
  implementation(projects.common.sdk.recipe.crud.api.external)
  implementation(projects.common.sdk.recipe.interaction.api.external)
}
