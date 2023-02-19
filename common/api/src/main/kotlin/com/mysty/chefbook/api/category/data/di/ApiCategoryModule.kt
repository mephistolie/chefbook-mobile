package com.mysty.chefbook.api.category.data.di

import com.mysty.chefbook.api.category.data.cache.CategoriesCache
import com.mysty.chefbook.api.category.data.cache.ICategoriesCache
import com.mysty.chefbook.api.category.data.cache.ICategoriesCacheReader
import com.mysty.chefbook.api.category.data.cache.ICategoriesCacheWriter
import com.mysty.chefbook.api.category.data.local.LocalCategorySource
import com.mysty.chefbook.api.category.data.remote.RemoteCategorySource
import com.mysty.chefbook.api.category.data.remote.api.CategoryApi
import com.mysty.chefbook.api.category.data.repository.CategoryRepo
import com.mysty.chefbook.api.category.data.repository.ILocalCategorySource
import com.mysty.chefbook.api.category.data.repository.IRemoteCategorySource
import com.mysty.chefbook.api.category.domain.ICategoryRepo
import com.mysty.chefbook.api.category.domain.usecases.CreateCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.DeleteCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.GetCategoriesUseCase
import com.mysty.chefbook.api.category.domain.usecases.GetCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.ICreateCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IDeleteCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoriesUseCase
import com.mysty.chefbook.api.category.domain.usecases.IGetCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.IObserveCategoriesUseCase
import com.mysty.chefbook.api.category.domain.usecases.IUpdateCategoryUseCase
import com.mysty.chefbook.api.category.domain.usecases.ObserveCategoriesUseCase
import com.mysty.chefbook.api.category.domain.usecases.UpdateCategoryUseCase
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val apiCategoryModule = module {

    singleOf(::CategoriesCache) binds arrayOf(ICategoriesCache::class, ICategoriesCacheReader::class, ICategoriesCacheWriter::class)

    single { createCategoryApi(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::LocalCategorySource) { named(Qualifiers.LOCAL) } bind ILocalCategorySource::class
    singleOf(::RemoteCategorySource) { named(Qualifiers.REMOTE) } bind IRemoteCategorySource::class

    single<ICategoryRepo> { CategoryRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }

    factoryOf(::ObserveCategoriesUseCase) bind IObserveCategoriesUseCase::class
    factoryOf(::GetCategoriesUseCase) bind IGetCategoriesUseCase::class
    factoryOf(::GetCategoryUseCase) bind IGetCategoryUseCase::class
    factoryOf(::CreateCategoryUseCase) bind ICreateCategoryUseCase::class
    factoryOf(::UpdateCategoryUseCase) bind IUpdateCategoryUseCase::class
    factoryOf(::DeleteCategoryUseCase) bind IDeleteCategoryUseCase::class

}

private fun createCategoryApi(retrofit: Retrofit) = retrofit.create(CategoryApi::class.java)
