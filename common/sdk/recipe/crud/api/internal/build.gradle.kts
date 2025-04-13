plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.crud.api.external)

      api(projects.common.sdk.recipe.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.crud.api.internal"
