package io.chefbook.sdk.encryption.recipe.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.encryption.recipe.api.internal.data.crypto.RecipeCryptor
import io.chefbook.sdk.encryption.recipe.api.internal.data.repositories.RecipeEncryptionRepository
import io.chefbook.sdk.encryption.recipe.impl.data.crypto.RecipeCryptorImpl
import io.chefbook.sdk.encryption.recipe.impl.data.repositories.RecipeEncryptionRepositoryImpl
import io.chefbook.sdk.encryption.recipe.impl.data.sources.RecipeEncryptionSource
import io.chefbook.sdk.encryption.recipe.impl.data.sources.local.LocalRecipeEncryptionSource
import io.chefbook.sdk.encryption.recipe.impl.data.sources.local.LocalRecipeEncryptionSourceImpl
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.RemoteRecipeEncryptionSourceImpl
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.RecipeEncryptionApiService
import io.chefbook.sdk.encryption.recipe.impl.data.sources.remote.services.RecipeEncryptionApiServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkRecipeEncryptionModule = module {

  single<RecipeCryptor> { RecipeCryptorImpl }

  singleOf(::RecipeEncryptionApiServiceImpl) bind RecipeEncryptionApiService::class

  single<LocalRecipeEncryptionSource>(named(DataSource.LOCAL)) { LocalRecipeEncryptionSourceImpl(get()) }
  single<RecipeEncryptionSource>(named(DataSource.REMOTE)) { RemoteRecipeEncryptionSourceImpl(get()) }

  single<RecipeEncryptionRepository> {
    RecipeEncryptionRepositoryImpl(
      get(named(DataSource.LOCAL)),
      get(named(DataSource.REMOTE)),
      get(),
    )
  }
}
