package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.common.ChefBookDatabase
import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.repositories.local.ChefBookLocalDataSource
import com.cactusknights.chefbook.repositories.remote.ChefBookRemoteDataSource
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.datasources.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesModule {

    @Provides
    @Singleton
    fun provideLocalContentDataSource(roomDatabase: ChefBookDatabase) : LocalDataSource =
        ChefBookLocalDataSource(roomDatabase.chefBookDao())

    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        api: ChefBookApi,
        authInterceptor: AuthInterceptor
    ): RemoteAuthDataSource {
        return RemoteAuthDataSource(api, authInterceptor)
    }

    @Provides
    @Singleton
    fun provideRemoteUserDataSource(api: ChefBookApi): RemoteUserDataSource {
        return RemoteUserDataSource(api)
    }

    @Provides
    @Singleton
    fun provideRemoteRecipesDataSource(api: ChefBookApi): RemoteRecipesDataSource {
        return RemoteRecipesDataSource(api)
    }

    @Provides
    @Singleton
    fun provideRemoteCategoriesDataSource(api: ChefBookApi): RemoteCategoriesDataSource {
        return RemoteCategoriesDataSource(api)
    }

    @Provides
    @Singleton
    fun provideRemoteShoppingListDataSource(api: ChefBookApi): RemoteShoppingListDataSource {
        return RemoteShoppingListDataSource(api)
    }

    @Provides
    @Singleton
    fun provideRemoteContentDataSource(
        ads: RemoteAuthDataSource,
        uds: RemoteUserDataSource,
        rds: RemoteRecipesDataSource,
        cds: RemoteCategoriesDataSource,
        slds: RemoteShoppingListDataSource
    ): RemoteDataSource {
        return ChefBookRemoteDataSource(ads, uds, rds, cds, slds)
    }
}