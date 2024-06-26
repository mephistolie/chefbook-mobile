plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.core.api.external)

      api(projects.common.sdk.tag.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.core.api.internal"
