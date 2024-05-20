plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.profile.api.external)

      api(projects.common.sdk.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.profile.api.internal"
