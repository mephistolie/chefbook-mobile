plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.category.api.external)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.category.api.internal"
