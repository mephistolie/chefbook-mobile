plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.recipe.book.api.internal)

      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.recipe.crud.api.internal)
      implementation(projects.common.sdk.recipe.interaction.api.internal)
      implementation(projects.common.sdk.collection.api.internal)
      implementation(projects.common.sdk.encryption.vault.api.internal)
      implementation(projects.common.sdk.encryption.recipe.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.recipe.book.impl"
