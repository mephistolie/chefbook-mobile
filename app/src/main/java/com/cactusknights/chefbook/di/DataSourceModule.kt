package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.IAuthSource
import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.data.ILocalCategorySource
import com.cactusknights.chefbook.data.ILocalEncryptionSource
import com.cactusknights.chefbook.data.ILocalProfileSource
import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.ILocalRecipeSource
import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.data.IRemoteCategorySource
import com.cactusknights.chefbook.data.IRemoteEncryptionSource
import com.cactusknights.chefbook.data.IRemoteProfileSource
import com.cactusknights.chefbook.data.IRemoteRecipeInteractionSource
import com.cactusknights.chefbook.data.IRemoteRecipeSource
import com.cactusknights.chefbook.data.IShoppingListSource
import com.cactusknights.chefbook.data.datasources.local.LocalCategorySource
import com.cactusknights.chefbook.data.datasources.local.LocalEncryptionSource
import com.cactusknights.chefbook.data.datasources.local.LocalFileSource
import com.cactusknights.chefbook.data.datasources.local.LocalProfileSource
import com.cactusknights.chefbook.data.datasources.local.LocalShoppingListSource
import com.cactusknights.chefbook.data.datasources.local.recipes.LocalRecipeInteractionSource
import com.cactusknights.chefbook.data.datasources.local.recipes.LocalRecipePictureSource
import com.cactusknights.chefbook.data.datasources.local.recipes.LocalRecipeSource
import com.cactusknights.chefbook.data.datasources.remote.RemoteAuthSource
import com.cactusknights.chefbook.data.datasources.remote.RemoteCategorySource
import com.cactusknights.chefbook.data.datasources.remote.RemoteEncryptionSource
import com.cactusknights.chefbook.data.datasources.remote.RemoteFileSource
import com.cactusknights.chefbook.data.datasources.remote.RemoteProfileSource
import com.cactusknights.chefbook.data.datasources.remote.RemoteShoppingListSource
import com.cactusknights.chefbook.data.datasources.remote.recipes.RemoteRecipeInteractionSource
import com.cactusknights.chefbook.data.datasources.remote.recipes.RemoteRecipePictureSource
import com.cactusknights.chefbook.data.datasources.remote.recipes.RemoteRecipeSource
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {

    single(named(Qualifiers.REMOTE)) { RemoteAuthSource(get(), get()) } bind IAuthSource::class

    single(named(Qualifiers.LOCAL)) { LocalProfileSource(get(named(Qualifiers.DataStore.PROFILE))) } bind ILocalProfileSource::class
    single(named(Qualifiers.REMOTE)) { RemoteProfileSource(get(), get()) } bind IRemoteProfileSource::class

    single(named(Qualifiers.LOCAL)) { LocalEncryptionSource(get()) } bind ILocalEncryptionSource::class
    single(named(Qualifiers.REMOTE)) { RemoteEncryptionSource(get(), get(named(Qualifiers.REMOTE)), get()) } bind IRemoteEncryptionSource::class

    single(named(Qualifiers.LOCAL)) { LocalFileSource(get()) } bind IFileSource::class
    single(named(Qualifiers.REMOTE)) { RemoteFileSource(get()) } bind IFileSource::class

    single(named(Qualifiers.LOCAL)) { LocalRecipeSource(get()) } bind ILocalRecipeSource::class
    single(named(Qualifiers.REMOTE)) { RemoteRecipeSource(get(), get()) } bind IRemoteRecipeSource::class

    single(named(Qualifiers.LOCAL)) { LocalRecipePictureSource(get(), get()) } bind IRecipePictureSource::class
    single(named(Qualifiers.REMOTE)) { RemoteRecipePictureSource(get(), get()) } bind IRecipePictureSource::class

    single(named(Qualifiers.LOCAL)) { LocalRecipeInteractionSource(get()) } bind ILocalRecipeInteractionSource::class
    single(named(Qualifiers.REMOTE)) { RemoteRecipeInteractionSource(get(), get()) } bind IRemoteRecipeInteractionSource::class

    single(named(Qualifiers.LOCAL)) { LocalCategorySource(get()) } bind ILocalCategorySource::class
    single(named(Qualifiers.REMOTE)) { RemoteCategorySource(get(), get()) } bind IRemoteCategorySource::class

    single(named(Qualifiers.LOCAL)) { LocalShoppingListSource(get(named(Qualifiers.DataStore.SHOPPING_LIST))) } bind IShoppingListSource::class
    single(named(Qualifiers.REMOTE)) { RemoteShoppingListSource(get(), get()) } bind IShoppingListSource::class

}
