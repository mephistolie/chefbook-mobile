import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.compose)
  alias(libs.plugins.module.android.navigation)
}

android.namespace = "io.chefbook.ui.common"
composeDestinationsModuleName("ui")

dependencies {
  api(projects.common.libs.models)

  implementation(projects.common.libs.exceptions)

  implementation(projects.common.ui.utils)
  implementation(projects.android.core)
  implementation(projects.android.design)
  implementation(projects.android.navigation)

  implementation(libs.androidx.compose.uiTooling)
  implementation(libs.androidx.compose.material)

  implementation(libs.compost.ui)

  implementation(libs.coil.compose)
}
