package com.cactusknights.chefbook.di

import android.content.Context
import android.content.SharedPreferences
import com.cactusknights.chefbook.ChefBookRepository
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.repositories.remote.datasources.AuthInterceptor
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteAuthDataSource
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteRecipesDataSource
import com.cactusknights.chefbook.repositories.remote.api.ChefBookApi
import com.cactusknights.chefbook.repositories.remote.datasources.RemoteShoppingListDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }
}