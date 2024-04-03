plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.shoppingList.api.internal)

      implementation(projects.common.sdk.profile.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.androidx.datastore)
    }
  }
}

android.namespace = "io.chefbook.sdk.shoppinglist.impl"
