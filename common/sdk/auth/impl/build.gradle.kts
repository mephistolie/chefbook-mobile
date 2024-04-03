plugins {
  id("multiplatform-network-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.auth.api.external)
      api(projects.common.sdk.auth.api.internal)

      implementation(projects.common.sdk.core.api.internal)
      implementation(projects.common.sdk.network.api.internal)
      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.profile.api.internal)

      implementation(libs.network.ktor.client.auth)
    }
    androidMain.dependencies {
      implementation(libs.androidx.datastore)
    }
  }
}

android.namespace = "io.chefbook.sdk.auth.impl"
