plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.settings.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.api.internal"
