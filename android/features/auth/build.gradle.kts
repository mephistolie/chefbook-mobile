plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.auth"
composeDestinationsModuleName("auth")

dependencies {
  implementation(projects.common.libs.utils)
  implementation(projects.common.sdk.auth.api.external)

  implementation(libs.androidx.compose.uiTooling)
}
