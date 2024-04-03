plugins {
  id("multiplatform-network-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.network.api.internal)
      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.auth.api.internal)

      implementation(libs.network.ktor.client.auth)
      implementation(libs.network.ktor.client.logging)
      implementation(libs.network.ktor.client.contentNegotiation)
      implementation(libs.network.ktor.serialization.kotlinx.json)
    }
    androidMain.dependencies {
      implementation(projects.common.sdk.encryption.vault.api.internal)
      implementation(projects.common.sdk.encryption.recipe.api.internal)

      implementation(libs.network.ktor.client.okhttp)
    }
    iosMain.dependencies {
      implementation(libs.network.ktor.client.darwin)
    }
  }
}

android {
  namespace = "io.chefbook.sdk.network.impl"

  buildFeatures {
    buildConfig = true
  }
}
