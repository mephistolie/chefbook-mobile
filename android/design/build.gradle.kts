plugins {
  id("android-compose-module")
}

android.namespace = "io.chefbook.design"

dependencies {
  implementation(projects.android.core)

  implementation(libs.network.ktor.client.okhttp)

  implementation(libs.androidx.compose.uiTooling)

  implementation(libs.compost.ui)

  implementation(libs.coil.compose)
}
