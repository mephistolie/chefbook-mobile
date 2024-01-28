plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.profile.control"
composeDestinationsModuleName("profile-control")

dependencies {
  implementation(projects.common.sdk.profile.api.external)
  implementation(projects.common.sdk.auth.api.external)
}
