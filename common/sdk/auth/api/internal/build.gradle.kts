plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.network.ktor.client.auth)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.auth.api.internal"
