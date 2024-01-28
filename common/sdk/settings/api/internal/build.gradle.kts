plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.settings.api.external)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.api.internal"
