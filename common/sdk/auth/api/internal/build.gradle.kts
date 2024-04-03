plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.network.ktor.client.auth)
    }
  }
}

android.namespace = "io.chefbook.sdk.auth.api.internal"
