plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.category.api.internal)

        implementation(projects.common.sdk.auth.api.internal)
        implementation(projects.common.sdk.profile.api.internal)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.category.impl"
