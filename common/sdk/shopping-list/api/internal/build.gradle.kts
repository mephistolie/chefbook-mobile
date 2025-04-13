plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.shoppingList.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.shoppinglist.api.internal"
