plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.core.api.external)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.community.api.external"
