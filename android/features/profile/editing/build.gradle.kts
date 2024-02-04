plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.profile.editing"
composeDestinationsModuleName("profile-editing")

dependencies {
  implementation(projects.common.sdk.profile.api.external)

  implementation(libs.coil.compose)
  implementation(libs.imageCropper)
}
