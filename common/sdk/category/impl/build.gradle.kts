plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.category.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.profile.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.category.impl"
