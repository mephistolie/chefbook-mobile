package io.chefbook.sdk.encryption.recipe.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.libs.di.scopes.ProfileComponent
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
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

fun sdkRecipeEncryptionModule() = module {

  single<RecipeCryptor> { RecipeCryptorImpl }

  scope<ProfileComponent> {

    scopedOf(::RecipeEncryptionApiServiceImpl) bind RecipeEncryptionApiService::class

    scoped<LocalRecipeEncryptionSource>(named(DataSource.LOCAL)) {
      LocalRecipeEncryptionSourceImpl(
        profileId = get<ProfileComponent>().profileId,
        io = get(),
      )
    }

    scoped<RecipeEncryptionSource>(named(DataSource.REMOTE)) {
      RemoteRecipeEncryptionSourceImpl(
        api = get(),
      )
    }

    scoped<RecipeEncryptionRepository> {
      RecipeEncryptionRepositoryImpl(
        localSource = get(named(DataSource.LOCAL)),
        remoteSource = get(named(DataSource.REMOTE)),
        sources = get(),
      )
    }
  }
}
