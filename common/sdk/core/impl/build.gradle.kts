plugins {
  alias(libs.plugins.module.multiplatform.data)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.core.api.internal)

      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.profile.api.internal)
      implementation(projects.common.sdk.recipe.book.api.internal)
      implementation(projects.common.sdk.encryption.vault.api.internal)
      implementation(projects.common.sdk.encryption.recipe.api.internal)
      implementation(projects.common.sdk.collection.api.internal)
      implementation(projects.common.sdk.shoppingList.api.internal)
    }
  }
}

android.namespace = "io.chefbook.sdk.core.impl"
