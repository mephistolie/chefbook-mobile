package com.cactusknights.chefbook.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cactusknights.chefbook.common.ChefBookDatabase
import com.cactusknights.chefbook.repositories.local.datasources.*
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.datasources.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesModule {

    @Provides
    @Singleton
    fun provideLocalContentDataSource() : LocalUsersDataSource =
        LocalUsersDataSource()

    @Provides
    @Singleton
    fun provideLocalRecipesDataSource(roomDatabase: ChefBookDatabase, ds: DataStore<Preferences>, gson: Gson) : LocalRecipesDataSource =
        LocalRecipesDataSource(roomDatabase.recipesDao(), gson, ds)

    @Provides
    @Singleton
    fun provideLocalEncryptionDataSource(@ApplicationContext context: Context) : LocalEncryptionDataSource =
        LocalEncryptionDataSource(context)

    @Provides
    @Singleton
    fun provideLocalCategoriesDataSource(roomDatabase: ChefBookDatabase, ds: DataStore<Preferences>, gson: Gson) : LocalCategoriesDataSource =
        LocalCategoriesDataSource(roomDatabase.categoriesDao(), ds, gson)

    @Provides
    @Singleton
    fun provideLocalShoppingListDataSource(ds: DataStore<Preferences>, gson: Gson) : LocalShoppingListDataSource =
        LocalShoppingListDataSource(ds, gson)

    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        api: ChefBookApi,
        sp: SharedPreferences
    ): RemoteAuthDataSource {
        return RemoteAuthDataSource(api, sp)
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
    fun provideRemoteEncryptionDataSource(api: ChefBookApi) : RemoteEncryptionDataSource =
        RemoteEncryptionDataSource(api)

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
}