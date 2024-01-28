plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.common.sdk.recipe.book.api.internal)

        implementation(projects.common.sdk.profile.api.internal)
        implementation(projects.common.sdk.recipe.crud.api.internal)
        implementation(projects.common.sdk.recipe.interaction.api.internal)
        implementation(projects.common.sdk.category.api.internal)
        implementation(projects.common.sdk.encryption.vault.api.internal)
        implementation(projects.common.sdk.encryption.recipe.api.internal)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.androidx.datastore)
      }
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.book.impl"
