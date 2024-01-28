package io.chefbook.sdk.recipe.book.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetLatestRecipesUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.GetRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveLatestRecipesUseCase
import io.chefbook.sdk.recipe.book.api.external.domain.usecases.ObserveRecipeBookUseCase
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.LatestRecipesRepository
import io.chefbook.sdk.recipe.book.api.internal.data.repositories.RecipeBookRepository
import io.chefbook.sdk.recipe.book.impl.data.repositories.LatestRecipesRepositoryImpl
import io.chefbook.sdk.recipe.book.impl.data.repositories.RecipeBookRepositoryImpl
import io.chefbook.sdk.recipe.book.impl.data.sources.local.LocalRecipeBookSource
import io.chefbook.sdk.recipe.book.impl.data.sources.local.LocalRecipeBookSourceImpl
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.RemoteRecipeBookSource
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.RemoteRecipeBookSourceImpl
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.RecipeBookApiService
import io.chefbook.sdk.recipe.book.impl.data.sources.remote.services.RecipeBookApiServiceImpl
import io.chefbook.sdk.recipe.book.impl.domain.usecases.GetLatestRecipesUseCaseImpl
import io.chefbook.sdk.recipe.book.impl.domain.usecases.GetRecipeBookUseCaseImpl
import io.chefbook.sdk.recipe.book.impl.domain.usecases.ObserveLatestRecipesUseCaseImpl
import io.chefbook.sdk.recipe.book.impl.domain.usecases.ObserveRecipeBookUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkRecipeBookModule = module {

  singleOf(::RecipeBookApiServiceImpl) bind RecipeBookApiService::class

  single<LocalRecipeBookSource>(named(DataSource.LOCAL)) { LocalRecipeBookSourceImpl(get()) }
  single<RemoteRecipeBookSource>(named(DataSource.REMOTE)) { RemoteRecipeBookSourceImpl(get()) }

  single<RecipeBookRepository> {
    RecipeBookRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),

      localCrudSource = get(named(DataSource.LOCAL)),
      remoteCrudSource = get(named(DataSource.REMOTE)),
      localInteractionSource = get(named(DataSource.LOCAL)),

      sources = get(),
      cache = get(),
      encryptedVaultRepository = get(),
      recipeEncryptionRepository = get(),
      categoriesRepository = get(),
      cryptor = get(),
      dispatchers = get(),
      scopes = get(),
    )
  }
  singleOf(::LatestRecipesRepositoryImpl) bind LatestRecipesRepository::class

  factoryOf(::ObserveRecipeBookUseCaseImpl) bind ObserveRecipeBookUseCase::class
  factoryOf(::GetRecipeBookUseCaseImpl) bind GetRecipeBookUseCase::class
  factoryOf(::ObserveLatestRecipesUseCaseImpl) bind ObserveLatestRecipesUseCase::class
  factoryOf(::GetLatestRecipesUseCaseImpl) bind GetLatestRecipesUseCase::class
}
