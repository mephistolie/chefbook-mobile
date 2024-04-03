plugins {
  id("multiplatform-network-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.settings.api.internal)

      implementation(projects.common.sdk.core.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.androidx.datastore)
    }
  }
}

android.namespace = "io.chefbook.sdk.settings.impl"
