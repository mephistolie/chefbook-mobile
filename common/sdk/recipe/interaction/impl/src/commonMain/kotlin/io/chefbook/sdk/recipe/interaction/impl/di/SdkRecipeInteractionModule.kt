package io.chefbook.sdk.recipe.interaction.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeCategoriesUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeFavouriteStatusUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeSavedStatusUseCase
import io.chefbook.sdk.recipe.interaction.api.external.domain.usecases.SetRecipeScoreUseCase
import io.chefbook.sdk.recipe.interaction.api.internal.data.sources.local.LocalRecipeInteractionSource
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepository
import io.chefbook.sdk.recipe.interaction.impl.data.repositories.RecipeInteractionRepositoryImpl
import io.chefbook.sdk.recipe.interaction.impl.data.sources.local.LocalRecipeInteractionSourceImpl
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.RemoteRecipeInteractionSource
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.RemoteRecipeInteractionSourceImpl
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.RecipeInteractionApiService
import io.chefbook.sdk.recipe.interaction.impl.data.sources.remote.services.RecipeInteractionApiServiceImpl
import io.chefbook.sdk.recipe.interaction.impl.domain.usecases.SetRecipeCategoriesUseCaseImpl
import io.chefbook.sdk.recipe.interaction.impl.domain.usecases.SetRecipeFavouriteStatusUseCaseImpl
import io.chefbook.sdk.recipe.interaction.impl.domain.usecases.SetRecipeSavedStatusUseCaseImpl
import io.chefbook.sdk.recipe.interaction.impl.domain.usecases.SetRecipeScoreUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkRecipeInteractionModule = module {

  singleOf(::RecipeInteractionApiServiceImpl) bind RecipeInteractionApiService::class

  single<LocalRecipeInteractionSource>(named(DataSource.LOCAL)) { LocalRecipeInteractionSourceImpl(get()) }
  single<RemoteRecipeInteractionSource>(named(DataSource.REMOTE)) { RemoteRecipeInteractionSourceImpl(get()) }

  single<RecipeInteractionRepository> {
    RecipeInteractionRepositoryImpl(
      get(named(DataSource.LOCAL)),
      get(named(DataSource.REMOTE)),

      get(),
      get(),
    )
  }

  factoryOf(::SetRecipeScoreUseCaseImpl) bind SetRecipeScoreUseCase::class
  factoryOf(::SetRecipeSavedStatusUseCaseImpl) bind SetRecipeSavedStatusUseCase::class
  factoryOf(::SetRecipeFavouriteStatusUseCaseImpl) bind SetRecipeFavouriteStatusUseCase::class
  factoryOf(::SetRecipeCategoriesUseCaseImpl) bind SetRecipeCategoriesUseCase::class
}
