plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.category"
composeDestinationsModuleName("category")

dependencies {
  implementation(projects.common.sdk.category.api.external)

  implementation(libs.androidx.compose.uiTooling)
}
