plugins {
  alias(libs.plugins.module.multiplatform.network)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.book.api.external)

      api(projects.common.sdk.recipe.core.api.internal)
    }
  }
}
