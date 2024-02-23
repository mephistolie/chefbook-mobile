package io.chefbook.di

import io.chefbook.features.about.di.featureAboutModule
import io.chefbook.features.auth.di.featureAuthModule
import io.chefbook.features.category.di.featureCategoryModule
import io.chefbook.features.community.languages.di.featureCommunityLanguagesModule
import io.chefbook.features.community.recipes.di.featureCommunityRecipesModule
import io.chefbook.features.encryption.di.featureEncryptionModule
import io.chefbook.features.profile.control.di.featureProfileModule
import io.chefbook.features.profile.editing.di.featureProfileEditingModule
import io.chefbook.features.recipe.control.di.featureRecipeControlModule
import io.chefbook.features.recipe.info.di.featureRecipeModule
import io.chefbook.features.recipe.input.di.featureRecipeInputModule
import io.chefbook.features.recipe.rating.di.featureRecipeRatingModule
import io.chefbook.features.recipe.share.di.featureRecipeShareModule
import io.chefbook.features.recipebook.category.di.featureCategoryRecipesModule
import io.chefbook.features.recipebook.dashboard.di.featureRecipeBookDashboardModule
import io.chefbook.features.recipebook.favourite.di.featureFavouriteRecipesModule
import io.chefbook.features.recipebook.search.di.featureRecipeBookSearchModule
import io.chefbook.features.settings.di.featureSettingsModule
import io.chefbook.features.shoppinglist.control.di.featureShoppingListModule
import io.chefbook.features.shoppinglist.purchases.input.di.featurePurchaseInputModule
import io.chefbook.sdk.auth.impl.di.sdkAuthModule
import io.chefbook.sdk.category.impl.di.sdkCategoryModule
import io.chefbook.sdk.core.impl.di.sdkCoreModule
import io.chefbook.sdk.database.impl.di.sdkDatabaseModule
import io.chefbook.sdk.encryption.recipe.impl.di.sdkRecipeEncryptionModule
import io.chefbook.sdk.encryption.vault.impl.di.sdkEncryptedVaultModule
import io.chefbook.sdk.file.impl.di.sdkFileModule
import io.chefbook.sdk.network.impl.di.sdkNetworkModule
import io.chefbook.sdk.profile.impl.di.sdkProfileModule
import io.chefbook.sdk.recipe.book.impl.di.sdkRecipeBookModule
import io.chefbook.sdk.recipe.community.impl.di.sdkCommunityRecipesModule
import io.chefbook.sdk.recipe.core.impl.di.sdkRecipeCoreModule
import io.chefbook.sdk.recipe.crud.impl.di.sdkRecipeCrudModule
import io.chefbook.sdk.recipe.interaction.impl.di.sdkRecipeInteractionModule
import io.chefbook.sdk.settings.impl.di.sdkSettingsModule
import io.chefbook.sdk.shoppinglist.impl.di.sdkShoppingListModule
import io.chefbook.sdk.tag.impl.di.sdkTagModule

object Modules {

  val sdk = listOf(
    sdkCoreModule,
    sdkFileModule,
    sdkDatabaseModule,
    sdkNetworkModule,
    sdkSettingsModule,
    sdkAuthModule,
    sdkProfileModule,
    sdkEncryptedVaultModule,
    sdkRecipeEncryptionModule,
    sdkRecipeCoreModule,
    sdkRecipeCrudModule,
    sdkRecipeInteractionModule,
    sdkRecipeBookModule,
    sdkCommunityRecipesModule,
    sdkCategoryModule,
    sdkTagModule,
    sdkShoppingListModule,
  )

  object Features {

    val unscoped = listOf(
      featureEncryptionModule,
      featureAuthModule,
      featureProfileModule,
      featureProfileEditingModule,
      featureSettingsModule,
      featureCategoryModule,
      featureAboutModule,
    )

    val recipe = listOf(
      featureRecipeModule,
      featureRecipeShareModule,
      featureRecipeControlModule,
      featureRecipeRatingModule,
      featureRecipeInputModule,
    )

    val recipeBook = listOf(
      featureRecipeBookDashboardModule,
      featureRecipeBookSearchModule,
      featureFavouriteRecipesModule,
      featureCategoryRecipesModule,
    )

    val community = listOf(
      featureCommunityLanguagesModule,
      featureCommunityRecipesModule,
    )

    val shoppingList = listOf(
      featureShoppingListModule,
      featurePurchaseInputModule
    )

    val all = unscoped +
      recipe +
      recipeBook +
      community +
      shoppingList
  }

  val all = sdk +
    Features.all +
    appModule
}
