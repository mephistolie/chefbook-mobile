plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.profile.deletion"
composeDestinationsModuleName("profile-deletion")

dependencies {
  implementation(projects.common.sdk.profile.api.external)
}
