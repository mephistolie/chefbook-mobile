plugins {
  id("android-app-module")
}

android {
  namespace = "io.chefbook"
  defaultConfig {
    applicationId = "com.cactusknights.chefbook"
    versionCode = 45
    versionName = "4.0-alpha3"
  }
  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
    }
  }
}

dependencies {

  // Internal libs

  implementation(projects.common.libs.logger)

  implementation(projects.android.libs.mvi)

  // SDK

  implementation(projects.common.sdk.core.impl)
  implementation(projects.common.sdk.file.impl)
  implementation(projects.common.sdk.database.impl)
  implementation(projects.common.sdk.network.impl)
  implementation(projects.common.sdk.settings.impl)
  implementation(projects.common.sdk.auth.impl)
  implementation(projects.common.sdk.profile.impl)
  implementation(projects.common.sdk.encryption.vault.impl)
  implementation(projects.common.sdk.encryption.recipe.impl)
  implementation(projects.common.sdk.recipe.core.impl)
  implementation(projects.common.sdk.recipe.crud.impl)
  implementation(projects.common.sdk.recipe.interaction.impl)
  implementation(projects.common.sdk.recipe.book.impl)
  implementation(projects.common.sdk.recipe.community.impl)
  implementation(projects.common.sdk.category.impl)
  implementation(projects.common.sdk.tag.impl)
  implementation(projects.common.sdk.shoppingList.impl)

  implementation(projects.android.core)
  implementation(projects.android.design)
  implementation(projects.android.ui)
  implementation(projects.android.navigation)

  // Features

  implementation(projects.android.features.about)
  implementation(projects.android.features.settings)
  implementation(projects.android.features.auth)
  implementation(projects.android.features.profile.control)
  implementation(projects.android.features.profile.editing)
  implementation(projects.android.features.encryption)
  implementation(projects.android.features.recipeBook.dashboard)
  implementation(projects.android.features.recipeBook.favourite)
  implementation(projects.android.features.recipeBook.category)
  implementation(projects.android.features.recipeBook.search)
  implementation(projects.android.features.recipe.info)
  implementation(projects.android.features.recipe.control)
  implementation(projects.android.features.recipe.rating)
  implementation(projects.android.features.recipe.share)
  implementation(projects.android.features.recipe.input)
  implementation(projects.android.features.category)
  implementation(projects.android.features.community.languages)
  implementation(projects.android.features.community.recipes)
  implementation(projects.android.features.shoppingList.control)
  implementation(projects.android.features.shoppingList.purchaseInput)

  coreLibraryDesugaring(libs.jdk.desugar)
}
