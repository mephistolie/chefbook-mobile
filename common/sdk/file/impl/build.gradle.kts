plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.file.api.internal)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.compressor)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.file.impl"
