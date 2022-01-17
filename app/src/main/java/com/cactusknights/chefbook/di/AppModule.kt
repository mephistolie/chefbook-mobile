package com.cactusknights.chefbook.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.base.Constants.CHEFBOOK_SETTINGS
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(CHEFBOOK_SETTINGS )

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context, ): DataStore<Preferences> {

        val datastore = PreferenceDataStoreFactory.create(scope = CoroutineScope(Dispatchers.Default)) {
            context.preferencesDataStoreFile(CHEFBOOK_SETTINGS)
        }
        return datastore
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}