plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.core.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.crud.api.external"
