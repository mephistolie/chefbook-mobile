plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.libs.models)
    }
  }
}

android.namespace = "io.chefbook.sdk.shoppinglist.api.external"
