import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.profile.deletion"
composeDestinationsModuleName("profile-deletion")

dependencies {
  implementation(projects.common.sdk.profile.api.external)
}
