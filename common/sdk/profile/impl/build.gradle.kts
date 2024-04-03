plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.profile.api.internal)

      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.file.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.androidx.datastore)
    }
  }
}

android.namespace = "io.chefbook.sdk.profile.impl"
