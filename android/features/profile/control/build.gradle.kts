import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.profile.control"
composeDestinationsModuleName("profile-control")

dependencies {
  implementation(projects.common.sdk.profile.api.external)
  implementation(projects.common.sdk.auth.api.external)

  implementation(projects.common.features.auth)
}
