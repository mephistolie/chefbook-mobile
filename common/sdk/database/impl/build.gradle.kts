plugins {
  id("multiplatform-base-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.sdk.database.api.internal)
      implementation(libs.di.koin.core)
    }
    androidMain.dependencies {
      implementation(libs.database.sqldelight.driver.android)
    }
    iosMain.dependencies {
      implementation(libs.database.sqldelight.driver.native)
    }
  }
}

android.namespace = "io.chefbook.sdk.database.impl"

