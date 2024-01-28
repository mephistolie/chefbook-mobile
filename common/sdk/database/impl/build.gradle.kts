plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(projects.common.sdk.database.api.internal)
        implementation(libs.di.koin.core)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.database.sqldelight.driver.android)
      }
    }
    val iosMain by getting {
      dependencies {
        implementation(libs.database.sqldelight.driver.native)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.database.impl"

