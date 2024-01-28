plugins {
  id("multiplatform-network-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.interaction.api.external)

        api(projects.common.sdk.recipe.core.api.internal)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.interaction.api.internal"
