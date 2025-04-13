plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.core.api.external)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.community.api.external"
