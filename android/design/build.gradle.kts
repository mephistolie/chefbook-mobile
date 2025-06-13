plugins {
  alias(libs.plugins.module.android.compose)
}

android.namespace = "io.chefbook.design"

dependencies {
  api(projects.common.ui.design)

  implementation(projects.common.ui.utils)
  implementation(projects.android.core)

  implementation(libs.network.ktor.client.okhttp)

  implementation(libs.androidx.compose.materialIcons.core)
  implementation(libs.androidx.compose.uiTooling)

  implementation(libs.compost.ui)

  implementation(libs.coil.compose)
  implementation(libs.coil.network.ktor)
}
