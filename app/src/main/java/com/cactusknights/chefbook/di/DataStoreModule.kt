package com.cactusknights.chefbook.di

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.cactusknights.chefbook.data.datastore.LatestRecipesSerializer
import com.cactusknights.chefbook.data.datastore.ProfileSerializer
import com.cactusknights.chefbook.data.datastore.SettingsSerializer
import com.cactusknights.chefbook.data.datastore.ShoppingListSerializer
import com.cactusknights.chefbook.data.datastore.TokensSerializer
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

const val SETTINGS_FILE = "settings.proto"
const val TOKENS_FILE = "tokens.proto"
const val PROFILE_FILE = "profile.proto"
const val LATEST_RECIPES_FILE = "latest_recipes.proto"
const val SHOPPING_LIST_FILE = "shopping_list.proto"

val dataStoreModule = module {

    singleOf(::createSettingsDataStore) { named(Qualifiers.DataStore.SETTINGS) }
    singleOf(::createTokensDataStore) { named(Qualifiers.DataStore.TOKENS) }
    singleOf(::createProfileCacheDataStore) { named(Qualifiers.DataStore.PROFILE) }
    singleOf(::createLatestRecipesDataStore) { named(Qualifiers.DataStore.LATEST_RECIPES) }
    singleOf(::createShoppingListDataStore) { named(Qualifiers.DataStore.SHOPPING_LIST) }

}

private fun createSettingsDataStore(context: Context) = createDataStore(context, SETTINGS_FILE, SettingsSerializer)
private fun createTokensDataStore(context: Context) = createDataStore(context, TOKENS_FILE, TokensSerializer)
private fun createProfileCacheDataStore(context: Context) = createDataStore(context, PROFILE_FILE, ProfileSerializer)
private fun createLatestRecipesDataStore(context: Context) = createDataStore(context, LATEST_RECIPES_FILE, LatestRecipesSerializer)
private fun createShoppingListDataStore(context: Context) = createDataStore(context, SHOPPING_LIST_FILE, ShoppingListSerializer)

private fun <T> createDataStore(context: Context, file: String, serializer: Serializer<T>) =
    DataStoreFactory.create(
        serializer = serializer,
        produceFile = { context.dataStoreFile(file) }
    )
