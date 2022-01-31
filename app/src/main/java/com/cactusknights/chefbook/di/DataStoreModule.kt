package com.cactusknights.chefbook.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.ShoppingListProto
import com.cactusknights.chefbook.SyncDataProto
import com.cactusknights.chefbook.TokensProto
import com.cactusknights.chefbook.core.datastore.SettingsSerializer
import com.cactusknights.chefbook.core.datastore.ShoppingListSerializer
import com.cactusknights.chefbook.core.datastore.SyncDataSerializer
import com.cactusknights.chefbook.core.datastore.TokensSerializer
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    companion object {
        const val SETTINGS_FILE = "settings.proto"
        const val TOKENS_FILE = "tokens.proto"
        const val SYNC_DATA_FILE = "sync_data.proto"
        const val SHOPPING_LIST_FILE = "shopping_list.proto"
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<SettingsProto> {
        return DataStoreFactory.create(
            serializer = SettingsSerializer,
            produceFile = { context.dataStoreFile(SETTINGS_FILE) }
        )
    }

    @Provides
    @Singleton
    fun provideTokensDataStore(@ApplicationContext context: Context): DataStore<TokensProto> {
        return DataStoreFactory.create(
            serializer = TokensSerializer,
            produceFile = { context.dataStoreFile(TOKENS_FILE) }
        )
    }

    @Provides
    @Singleton
    fun provideSyncData(@ApplicationContext context: Context): DataStore<SyncDataProto> {
        return DataStoreFactory.create(
            serializer = SyncDataSerializer,
            produceFile = { context.dataStoreFile(SYNC_DATA_FILE) }
        )
    }

    @Provides
    @Singleton
    fun provideShoppingListDataStore(@ApplicationContext context: Context): DataStore<ShoppingListProto> {
        return DataStoreFactory.create(
            serializer = ShoppingListSerializer,
            produceFile = { context.dataStoreFile(SHOPPING_LIST_FILE) }
        )
    }
}