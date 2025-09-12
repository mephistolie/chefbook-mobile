import com.android.build.api.dsl.androidLibrary

plugins {
  alias(libs.plugins.module.multiplatform.base)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
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

      implementation(projects.common.libs.mvi)
      implementation(projects.common.libs.utils)
      implementation(projects.common.libs.logging)
      implementation(projects.common.libs.coroutines)
      implementation(projects.common.libs.exceptions)
      implementation(projects.common.libs.encryption)
      implementation(projects.common.libs.di)
      implementation(projects.common.libs.models)

      // SDK

      implementation(projects.common.sdk.core.api.internal)
      implementation(projects.common.sdk.core.impl)

      implementation(projects.common.sdk.network.api.internal)
      implementation(projects.common.sdk.network.impl)

      implementation(projects.common.sdk.file.api.internal)
      implementation(projects.common.sdk.file.impl)

      implementation(projects.common.sdk.database.api.internal)
      implementation(projects.common.sdk.database.impl)

      implementation(projects.common.sdk.settings.api.external)
      implementation(projects.common.sdk.settings.api.internal)
      implementation(projects.common.sdk.settings.impl)

      implementation(projects.common.sdk.auth.api.external)
      implementation(projects.common.sdk.auth.api.internal)
      implementation(projects.common.sdk.auth.impl)

      implementation(projects.common.sdk.profile.api.external)
      implementation(projects.common.sdk.profile.api.internal)
      implementation(projects.common.sdk.profile.impl)

      implementation(projects.common.sdk.collection.api.external)
      implementation(projects.common.sdk.collection.api.internal)
      implementation(projects.common.sdk.collection.impl)

      implementation(projects.common.sdk.tag.api.external)
      implementation(projects.common.sdk.tag.api.internal)
      implementation(projects.common.sdk.tag.impl)

      implementation(projects.common.sdk.encryption.vault.api.external)
      implementation(projects.common.sdk.encryption.vault.api.internal)
      implementation(projects.common.sdk.encryption.vault.impl)

      implementation(projects.common.sdk.encryption.recipe.api.internal)
      implementation(projects.common.sdk.encryption.recipe.impl)

      implementation(projects.common.sdk.recipe.core.api.external)
      implementation(projects.common.sdk.recipe.core.api.internal)
      implementation(projects.common.sdk.recipe.core.impl)

      implementation(projects.common.sdk.recipe.crud.api.external)
      implementation(projects.common.sdk.recipe.crud.api.internal)
      implementation(projects.common.sdk.recipe.crud.impl)

      implementation(projects.common.sdk.recipe.book.api.external)
      implementation(projects.common.sdk.recipe.book.api.internal)
      implementation(projects.common.sdk.recipe.book.impl)

      implementation(projects.common.sdk.recipe.community.api.external)
      implementation(projects.common.sdk.recipe.community.impl)

      implementation(projects.common.sdk.recipe.interaction.api.external)
      implementation(projects.common.sdk.recipe.interaction.api.internal)
      implementation(projects.common.sdk.recipe.interaction.impl)

      implementation(projects.common.sdk.shoppingList.api.external)
      implementation(projects.common.sdk.shoppingList.api.internal)
      implementation(projects.common.sdk.shoppingList.impl)

      // Features

      implementation(projects.common.ui.utils)
      implementation(projects.common.ui.design)
      implementation(projects.common.ui.common)

      implementation(projects.common.features.root)
      implementation(projects.common.features.auth)
      implementation(projects.common.features.profile.control)
    }
  }
}
