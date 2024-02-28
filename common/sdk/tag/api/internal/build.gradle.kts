plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.tag.api.external)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.tag.api.internal"
