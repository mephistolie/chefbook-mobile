package com.mysty.chefbook.api.files.data.di

import com.mysty.chefbook.api.files.data.IFileSource
import com.mysty.chefbook.api.files.data.local.LocalFileSource
import com.mysty.chefbook.api.files.data.remote.RemoteFileSource
import com.mysty.chefbook.api.files.data.remote.api.FileApi
import com.mysty.chefbook.api.files.data.repositories.FilesRepo
import com.mysty.chefbook.api.files.data.repositories.IFilesRepo
import com.mysty.chefbook.api.files.domain.usecases.IRefreshDataUseCase
import com.mysty.chefbook.api.files.domain.usecases.RefreshDataUseCase
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val apiFilesModule = module {

    single { createFileApi(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::LocalFileSource) { named(Qualifiers.LOCAL) } bind IFileSource::class
    singleOf(::RemoteFileSource) { named(Qualifiers.REMOTE) } bind IFileSource::class

    single<IFilesRepo> { FilesRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE))) }

    factoryOf(::RefreshDataUseCase) bind IRefreshDataUseCase::class

}

private fun createFileApi(retrofit: Retrofit) = retrofit.create(FileApi::class.java)
