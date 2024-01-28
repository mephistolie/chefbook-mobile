plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.crud.api.external)

        api(projects.common.sdk.recipe.core.api.internal)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.crud.api.internal"
