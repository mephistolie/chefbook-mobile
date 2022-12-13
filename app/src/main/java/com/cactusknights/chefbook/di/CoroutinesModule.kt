package com.cactusknights.chefbook.di

import com.mysty.chefbook.core.coroutines.AppDispatchers
import com.mysty.chefbook.core.coroutines.CoroutineScopes
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coroutinesModule = module {

    single(createdAtStart = true) { AppDispatchers() }
    singleOf(::CoroutineScopes) { createdAtStart() }

}
