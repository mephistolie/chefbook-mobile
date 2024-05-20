plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.community.api.external)

      implementation(projects.common.sdk.recipe.core.api.internal)
      implementation(projects.common.sdk.settings.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.community.impl"
