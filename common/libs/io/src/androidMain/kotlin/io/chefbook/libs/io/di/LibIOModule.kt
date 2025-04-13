package io.chefbook.libs.io.di

import io.chefbook.libs.io.IOProvider
import io.chefbook.libs.io.IOProviderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun libIOModule() = module {
  singleOf(::IOProviderImpl) bind IOProvider::class
}
