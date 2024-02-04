package io.chefbook.sdk.category.impl.di

import io.chefbook.libs.di.qualifiers.DataSource
import io.chefbook.sdk.category.api.external.domain.usecases.CreateCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.DeleteCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoriesUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.GetCategoryUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.ObserveCategoriesUseCase
import io.chefbook.sdk.category.api.external.domain.usecases.UpdateCategoryUseCase
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCache
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCacheReader
import io.chefbook.sdk.category.api.internal.data.cache.CategoriesCacheWriter
import io.chefbook.sdk.category.api.internal.data.repositories.CategoryRepository
import io.chefbook.sdk.category.impl.data.cache.CategoriesCacheImpl
import io.chefbook.sdk.category.impl.data.repositories.CategoryRepositoryImpl
import io.chefbook.sdk.category.impl.data.sources.local.LocalCategorySource
import io.chefbook.sdk.category.impl.data.sources.local.LocalCategorySourceImpl
import io.chefbook.sdk.category.impl.data.sources.remote.RemoteCategorySource
import io.chefbook.sdk.category.impl.data.sources.remote.RemoteCategorySourceImpl
import io.chefbook.sdk.category.impl.data.sources.remote.services.CategoryApiService
import io.chefbook.sdk.category.impl.data.sources.remote.services.CategoryApiServiceImpl
import io.chefbook.sdk.category.impl.domain.CreateCategoryUseCaseImpl
import io.chefbook.sdk.category.impl.domain.DeleteCategoryUseCaseImpl
import io.chefbook.sdk.category.impl.domain.GetCategoriesUseCaseImpl
import io.chefbook.sdk.category.impl.domain.GetCategoryUseCaseImpl
import io.chefbook.sdk.category.impl.domain.ObserveCategoriesUseCaseImpl
import io.chefbook.sdk.category.impl.domain.UpdateCategoryUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val sdkCategoryModule = module {

  singleOf(::CategoriesCacheImpl) binds arrayOf(
    CategoriesCache::class,
    CategoriesCacheReader::class,
    CategoriesCacheWriter::class
  )

  singleOf(::CategoryApiServiceImpl) bind CategoryApiService::class

  singleOf(::LocalCategorySourceImpl) { named(DataSource.LOCAL) } bind LocalCategorySource::class
  singleOf(::RemoteCategorySourceImpl) { named(DataSource.REMOTE) } bind RemoteCategorySource::class

  single<CategoryRepository> {
    CategoryRepositoryImpl(
      localSource = get(named(DataSource.LOCAL)),
      remoteSource = get(named(DataSource.REMOTE)),

      profileRepository = get(),
      sources = get(),
      cache = get(),
      scopes = get(),
    )
  }

  factoryOf(::ObserveCategoriesUseCaseImpl) bind ObserveCategoriesUseCase::class
  factoryOf(::GetCategoriesUseCaseImpl) bind GetCategoriesUseCase::class
  factoryOf(::GetCategoryUseCaseImpl) bind GetCategoryUseCase::class
  factoryOf(::CreateCategoryUseCaseImpl) bind CreateCategoryUseCase::class
  factoryOf(::UpdateCategoryUseCaseImpl) bind UpdateCategoryUseCase::class
  factoryOf(::DeleteCategoryUseCaseImpl) bind DeleteCategoryUseCase::class
}
