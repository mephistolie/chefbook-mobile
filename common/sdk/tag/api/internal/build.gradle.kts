plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.tag.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.tag.api.internal"
