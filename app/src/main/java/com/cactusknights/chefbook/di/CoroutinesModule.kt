package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.core.coroutines.AppDispatchers
import com.cactusknights.chefbook.core.coroutines.CoroutineScopes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesModule {

    @Provides
    @Singleton
    fun provideDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideCoroutineScopes(dispatchers: AppDispatchers): CoroutineScopes = CoroutineScopes(dispatchers)

}
