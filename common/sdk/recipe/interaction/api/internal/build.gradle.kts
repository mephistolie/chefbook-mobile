plugins {
  alias(libs.plugins.module.multiplatform.network)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.interaction.api.external)

      api(projects.common.sdk.recipe.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.interaction.api.internal"
