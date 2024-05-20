plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.file.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.compressor)
    }
  }
}

android.namespace = "io.chefbook.sdk.file.impl"
