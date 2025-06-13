plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.interaction.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
    }
  }
}
