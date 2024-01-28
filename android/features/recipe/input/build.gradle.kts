plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.recipe.input"
composeDestinationsModuleName("recipe-input")

dependencies {
  implementation(projects.common.libs.models)
  implementation(projects.common.sdk.settings.api.external)
  implementation(projects.common.sdk.recipe.crud.api.external)
  implementation(projects.common.sdk.encryption.vault.api.external)

  implementation(libs.androidx.compose.uiTooling)
  implementation(libs.compose.reorderable)
  implementation(libs.imageCropper)
  implementation(libs.coil.compose)
}
