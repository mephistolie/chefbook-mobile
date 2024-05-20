plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.category.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.category.api.internal"
