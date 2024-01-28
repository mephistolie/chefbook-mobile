plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.core.api.internal)

        implementation(projects.common.sdk.database.api.internal)
        implementation(projects.common.sdk.recipe.book.api.internal)
        implementation(projects.common.sdk.category.api.internal)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.core.impl"
