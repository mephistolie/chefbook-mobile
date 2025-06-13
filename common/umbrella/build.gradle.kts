plugins {
  alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ChefBookMPP"
      isStatic = true
    }
  }

  sourceSets {
    iosMain.dependencies {

      // Libs

      api(projects.common.libs.mvi)
      api(projects.common.libs.utils)
      api(projects.common.libs.logging)
      api(projects.common.libs.coroutines)
      api(projects.common.libs.exceptions)
      api(projects.common.libs.encryption)
      api(projects.common.libs.di)
      api(projects.common.libs.models)

      // SDK

      api(projects.common.sdk.core.api.internal)
      api(projects.common.sdk.core.impl)

      api(projects.common.sdk.network.api.internal)
      api(projects.common.sdk.network.impl)

      api(projects.common.sdk.file.api.internal)
      api(projects.common.sdk.file.impl)

      api(projects.common.sdk.database.api.internal)
      api(projects.common.sdk.database.impl)

      api(projects.common.sdk.settings.api.external)
      api(projects.common.sdk.settings.api.internal)
      api(projects.common.sdk.settings.impl)

      api(projects.common.sdk.auth.api.external)
      api(projects.common.sdk.auth.api.internal)
      api(projects.common.sdk.auth.impl)

      api(projects.common.sdk.profile.api.external)
      api(projects.common.sdk.profile.api.internal)
      api(projects.common.sdk.profile.impl)

      api(projects.common.sdk.collection.api.external)
      api(projects.common.sdk.collection.api.internal)
      api(projects.common.sdk.collection.impl)

      api(projects.common.sdk.tag.api.external)
      api(projects.common.sdk.tag.api.internal)
      api(projects.common.sdk.tag.impl)

      api(projects.common.sdk.encryption.vault.api.external)
      api(projects.common.sdk.encryption.vault.api.internal)
      api(projects.common.sdk.encryption.vault.impl)

      api(projects.common.sdk.encryption.recipe.api.internal)
      api(projects.common.sdk.encryption.recipe.impl)

      api(projects.common.sdk.recipe.core.api.external)
      api(projects.common.sdk.recipe.core.api.internal)
      api(projects.common.sdk.recipe.core.impl)

      api(projects.common.sdk.recipe.crud.api.external)
      api(projects.common.sdk.recipe.crud.api.internal)
      api(projects.common.sdk.recipe.crud.impl)

      api(projects.common.sdk.recipe.book.api.external)
      api(projects.common.sdk.recipe.book.api.internal)
      api(projects.common.sdk.recipe.book.impl)

      api(projects.common.sdk.recipe.community.api.external)
      api(projects.common.sdk.recipe.community.impl)

      api(projects.common.sdk.recipe.interaction.api.external)
      api(projects.common.sdk.recipe.interaction.api.internal)
      api(projects.common.sdk.recipe.interaction.impl)

      api(projects.common.sdk.shoppingList.api.external)
      api(projects.common.sdk.shoppingList.api.internal)
      api(projects.common.sdk.shoppingList.impl)

      // Features

      api(projects.common.ui.utils)
      api(projects.common.ui.design)

      api(projects.common.features.root)
      api(projects.common.features.auth)
      api(projects.common.features.profile)
    }
  }
}
