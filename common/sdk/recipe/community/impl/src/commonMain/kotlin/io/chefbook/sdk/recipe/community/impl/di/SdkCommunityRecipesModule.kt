package io.chefbook.sdk.recipe.community.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetFastRecipesUseCase
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetMostVotedRecipesUseCase
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetNewRecipesUseCase
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetRecipesUseCase
import io.chefbook.sdk.recipe.community.api.external.domain.usecases.GetTopRecipesUseCase
import io.chefbook.sdk.recipe.community.impl.data.repositories.CommunityRecipesRepository
import io.chefbook.sdk.recipe.community.impl.data.repositories.CommunityRecipesRepositoryImpl
import io.chefbook.sdk.recipe.community.impl.data.sources.CommunityRecipesSource
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.RemoteCommunityRecipesSourceImpl
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.CommunityRecipesApiService
import io.chefbook.sdk.recipe.community.impl.data.sources.remote.services.CommunityRecipesApiServiceImpl
import io.chefbook.sdk.recipe.community.impl.domain.GetFastRecipesUseCaseImpl
import io.chefbook.sdk.recipe.community.impl.domain.GetMostVotedRecipesUseCaseImpl
import io.chefbook.sdk.recipe.community.impl.domain.GetNewRecipesUseCaseImpl
import io.chefbook.sdk.recipe.community.impl.domain.GetRecipesUseCaseImpl
import io.chefbook.sdk.recipe.community.impl.domain.GetTopRecipesUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val sdkCommunityRecipesModule = module {

  singleOf(::CommunityRecipesApiServiceImpl) bind CommunityRecipesApiService::class

  factory<CommunityRecipesSource>(named(DataSource.REMOTE)) {
    RemoteCommunityRecipesSourceImpl(get())
  }

  single<CommunityRecipesRepository> {
    CommunityRecipesRepositoryImpl(source = get(named(DataSource.REMOTE)))
  }

  factoryOf(::GetRecipesUseCaseImpl) bind GetRecipesUseCase::class
  factoryOf(::GetNewRecipesUseCaseImpl) bind GetNewRecipesUseCase::class
  factoryOf(::GetTopRecipesUseCaseImpl) bind GetTopRecipesUseCase::class
  factoryOf(::GetMostVotedRecipesUseCaseImpl) bind GetMostVotedRecipesUseCase::class
  factoryOf(::GetFastRecipesUseCaseImpl) bind GetFastRecipesUseCase::class
}
