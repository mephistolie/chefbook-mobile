plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipebook.core"

dependencies {
  implementation(projects.common.sdk.recipe.core.api.external)

  implementation(libs.coil.compose)
}
