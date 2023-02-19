package com.mysty.chefbook.api.recipe.data.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.mysty.chefbook.api.auth.data.local.mappers.TokensSerializer
import com.mysty.chefbook.api.profile.data.local.dto.ProfileSerializer
import com.mysty.chefbook.api.recipe.data.datastore.LatestRecipesSerializer
import com.mysty.chefbook.api.settings.data.local.dto.SettingsSerializer
import com.mysty.chefbook.api.shoppinglist.data.local.dto.ShoppingListSerializer

const val SETTINGS_FILE = "settings.proto"
const val TOKENS_FILE = "tokens.proto"
const val PROFILE_FILE = "profile.proto"
const val LATEST_RECIPES_FILE = "latest_recipes.proto"
const val SHOPPING_LIST_FILE = "shopping_list.proto"

internal object DataStoreUtils {

    fun getSettingsDataStore(context: Context) = getDataStore(context, SETTINGS_FILE, SettingsSerializer)
    fun getTokensDataStore(context: Context) = getDataStore(context, TOKENS_FILE, TokensSerializer)
    fun getProfileCacheDataStore(context: Context) = getDataStore(context, PROFILE_FILE, ProfileSerializer)
    fun getLatestRecipesDataStore(context: Context) = getDataStore(context, LATEST_RECIPES_FILE, LatestRecipesSerializer)
    fun getShoppingListDataStore(context: Context) = getDataStore(context, SHOPPING_LIST_FILE, ShoppingListSerializer)

    private fun <T> getDataStore(context: Context, file: String, serializer: Serializer<T>) =
        DataStoreFactory.create(
            serializer = serializer,
            produceFile = { context.dataStoreFile(file) }
        )
}
