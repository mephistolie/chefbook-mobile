plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.shoppingList.api.external)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.shoppinglist.api.internal"
