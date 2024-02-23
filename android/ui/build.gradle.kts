plugins {
  id("android-compose-module")
  id("android-navigation-module")
}

android.namespace = "io.chefbook.ui.common"
composeDestinationsModuleName("ui")

dependencies {
  implementation(projects.common.libs.models)
  implementation(projects.common.libs.exceptions)

  implementation(projects.android.core)
  implementation(projects.android.design)
  implementation(projects.android.navigation)

  implementation(libs.androidx.compose.uiTooling)
  implementation(libs.androidx.compose.material)

  implementation(libs.compost.ui)

  implementation(libs.coil.compose)
}
