plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.encryption.recipe.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.recipe.core.api.internal)
      implementation(projects.common.sdk.file.api.internal)
    }
  }
}

