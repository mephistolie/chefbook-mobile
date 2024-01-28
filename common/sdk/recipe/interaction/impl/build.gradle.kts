plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.interaction.api.internal)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.datastore)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.interaction.impl"
