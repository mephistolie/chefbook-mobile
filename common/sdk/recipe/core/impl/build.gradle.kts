plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.core.api.internal)

      implementation(projects.common.sdk.database.api.internal)
      implementation(projects.common.sdk.recipe.book.api.internal)
      implementation(projects.common.sdk.collection.api.internal)
    }
  }
}
