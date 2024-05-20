package io.chefbook.sdk.recipe.crud.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.CreateRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeInputPictureUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.DeleteRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.GetRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.ObserveRecipesUseCase
import io.chefbook.sdk.recipe.crud.api.external.domain.usecases.UpdateRecipeUseCase
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.RecipeCrudSource
import io.chefbook.sdk.recipe.crud.api.internal.data.sources.local.LocalRecipeCrudSource
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepository
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipeCrudRepositoryImpl
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipePictureRepository
import io.chefbook.sdk.recipe.crud.impl.data.repositories.RecipePictureRepositoryImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.RecipePicturesSource
import io.chefbook.sdk.recipe.crud.impl.data.sources.local.LocalRecipeCrudSourceImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.local.LocalRecipePicturesSourceImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.RemoteRecipeCrudSource
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.RemoteRecipeCrudSourceImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.RemoteRecipePicturesSourceImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipeCrudApiService
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipeCrudApiServiceImpl
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipePicturesApiService
import io.chefbook.sdk.recipe.crud.impl.data.sources.remote.services.RecipePicturesApiServiceImpl
import io.chefbook.sdk.recipe.crud.impl.domain.CreateRecipeUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.DeleteRecipeInputPictureUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.DeleteRecipeUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.GetRecipeUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.ObserveRecipeUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.ObserveRecipesUseCaseImpl
import io.chefbook.sdk.recipe.crud.impl.domain.UpdateRecipeUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkRecipeCrudModule = module {

  singleOf(::RecipeCrudApiServiceImpl) bind RecipeCrudApiService::class
  singleOf(::RecipePicturesApiServiceImpl) bind RecipePicturesApiService::class

  single<LocalRecipeCrudSource>(named(DataSource.LOCAL)) { LocalRecipeCrudSourceImpl(get()) }
  single<RemoteRecipeCrudSource>(named(DataSource.REMOTE)) { RemoteRecipeCrudSourceImpl(get()) }.bind(RecipeCrudSource::class)
  single<RecipePicturesSource>(named(DataSource.LOCAL)) { LocalRecipePicturesSourceImpl(get()) }
  single<RecipePicturesSource>(named(DataSource.REMOTE)) { RemoteRecipePicturesSourceImpl(get()) }

  single<RecipeCrudRepository> {
    RecipeCrudRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),

      cache = get(),
      encryptedVaultRepository =  get(),
      recipeEncryptionRepository = get(),
      profileRepository = get(),
      sources = get(),
      cryptor = get(),
      scopes = get(),
    )
  }
  single<RecipePictureRepository> {
    RecipePictureRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),

      cache = get(),
      files = get(),
      sources = get(),
    )
  }

  factoryOf(::ObserveRecipesUseCaseImpl) bind ObserveRecipesUseCase::class
  factoryOf(::ObserveRecipeUseCaseImpl) bind ObserveRecipeUseCase::class
  factoryOf(::GetRecipeUseCaseImpl) bind GetRecipeUseCase::class
  factoryOf(::CreateRecipeUseCaseImpl) bind CreateRecipeUseCase::class
  factoryOf(::UpdateRecipeUseCaseImpl) bind UpdateRecipeUseCase::class
  factoryOf(::DeleteRecipeUseCaseImpl) bind DeleteRecipeUseCase::class

  factoryOf(::DeleteRecipeInputPictureUseCaseImpl) bind DeleteRecipeInputPictureUseCase::class
}
