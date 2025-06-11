plugins {
  alias(libs.plugins.module.android.compose)
  alias(libs.plugins.module.android.navigation)
}

android.namespace = "io.chefbook.navigation"

dependencies {
  implementation(projects.common.core)
  implementation(projects.android.core)
  implementation(projects.android.design)
}
