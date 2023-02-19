package com.mysty.chefbook.api.sources.data.di

import com.mysty.chefbook.api.sources.data.repositories.SourcesRepo
import com.mysty.chefbook.api.sources.domain.ISourcesRepo
import com.mysty.chefbook.api.sources.domain.usecases.ISwitchDataSourceUseCase
import com.mysty.chefbook.api.sources.domain.usecases.SwitchDataSourceUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiSourcesModule = module {

    single<ISourcesRepo> { SourcesRepo(get(), get(), get()) }

    factoryOf(::SwitchDataSourceUseCase) bind ISwitchDataSourceUseCase::class

}
