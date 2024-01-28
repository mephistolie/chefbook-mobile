plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.libs.models)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.shoppinglist.api.external"
