plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.sdk.database.api.internal)

      implementation(projects.common.libs.io)

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

