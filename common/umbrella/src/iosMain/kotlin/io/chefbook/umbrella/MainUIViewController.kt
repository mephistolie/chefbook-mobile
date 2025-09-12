package io.chefbook.umbrella

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.chefbook.features.auth.di.featureAuthModule
import io.chefbook.features.profile.control.di.featureProfileModule
import io.chefbook.features.root.component.RootComponentImpl
import io.chefbook.features.root.di.featureRootModule
import io.chefbook.features.root.ui.RootScreen
import io.chefbook.sdk.auth.impl.di.sdkAuthModule
import io.chefbook.sdk.collection.impl.di.sdkCollectionModule
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
import org.koin.core.context.startKoin

fun MainUIViewController() = ComposeUIViewController(
  configure = {
    startKoin {
      modules(
        sdkCoreModule(),
        sdkFileModule(),
        sdkDatabaseModule(),
        sdkNetworkModule(),
        sdkSettingsModule(),
        sdkAuthModule(),
        sdkProfileModule(),
        sdkEncryptedVaultModule(),
        sdkRecipeEncryptionModule(),
        sdkRecipeCoreModule(),
        sdkRecipeCrudModule(),
        sdkRecipeInteractionModule(),
        sdkRecipeBookModule(),
        sdkCommunityRecipesModule(),
        sdkCollectionModule(),
        sdkTagModule(),
        sdkShoppingListModule(),

        featureRootModule(),
        featureAuthModule(),
        featureProfileModule(),
      )
    }
  },
) {
  val component = RootComponentImpl(
    componentContext = DefaultComponentContext(LifecycleRegistry()),
  )
  RootScreen(component)
}
