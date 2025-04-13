plugins {
  alias(libs.plugins.module.android.compose)
}

android.namespace = "io.chefbook.core.android"

dependencies {
  api(projects.android.libs.mvi)

  implementation(libs.network.ktor.client.okhttp)

  implementation(libs.androidx.compose.animation)

  implementation(libs.compose.shimmer)

  implementation(libs.zxing.core)
}
