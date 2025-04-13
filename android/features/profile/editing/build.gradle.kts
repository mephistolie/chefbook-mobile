import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.profile.editing"
composeDestinationsModuleName("profile-editing")

dependencies {
  implementation(projects.common.sdk.profile.api.external)

  implementation(libs.coil.compose)
  implementation(libs.imageCropper)
}
