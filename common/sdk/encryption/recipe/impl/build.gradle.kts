plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.encryption.recipe.api.internal)

      implementation(projects.common.sdk.profile.api.internal)
      implementation(projects.common.sdk.recipe.core.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.encryption.recipe.impl"
