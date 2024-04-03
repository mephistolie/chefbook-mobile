plugins {
  id("multiplatform-data-module")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.book.api.internal)

      implementation(projects.common.sdk.profile.api.internal)
      implementation(projects.common.sdk.recipe.crud.api.internal)
      implementation(projects.common.sdk.recipe.interaction.api.internal)
      implementation(projects.common.sdk.category.api.internal)
      implementation(projects.common.sdk.encryption.vault.api.internal)
      implementation(projects.common.sdk.encryption.recipe.api.internal)
    }
    androidMain.dependencies {
      implementation(libs.androidx.datastore)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.book.impl"
