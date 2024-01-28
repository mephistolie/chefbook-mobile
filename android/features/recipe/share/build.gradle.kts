plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipe.share"
composeDestinationsModuleName("recipe-share")

dependencies {
  implementation(projects.common.sdk.recipe.crud.api.external)

  implementation(libs.coil.compose)
}
