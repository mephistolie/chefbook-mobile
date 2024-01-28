plugins {
  id("multiplatform-network-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.settings.api.internal)

        implementation(projects.common.sdk.core.api.internal)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.datastore)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.impl"
