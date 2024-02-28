plugins {
  id("multiplatform-sdk-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.category.api.external)
        api(projects.common.sdk.tag.api.external)

        api(projects.common.libs.models)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.core.api.external"
