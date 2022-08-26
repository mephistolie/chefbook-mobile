package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.IAuthSource
import com.cactusknights.chefbook.data.IEncryptionSource
import com.cactusknights.chefbook.data.IFileSource
import com.cactusknights.chefbook.data.ILocalCategorySource
import com.cactusknights.chefbook.data.ILocalProfileSource
import com.cactusknights.chefbook.data.ILocalRecipeInteractionSource
import com.cactusknights.chefbook.data.ILocalRecipeSource
import com.cactusknights.chefbook.data.IRecipeInteractionSource
import com.cactusknights.chefbook.data.IRecipePictureSource
import com.cactusknights.chefbook.data.IRemoteCategorySource
import com.cactusknights.chefbook.data.IRemoteProfileSource
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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceBindModule {

    @Binds
    @Remote
    fun bindRemoteAuthSource(source: RemoteAuthSource): IAuthSource

    @Binds
    @Local
    fun bindLocalProfileSource(source: LocalProfileSource): ILocalProfileSource

    @Binds
    @Remote
    fun bindRemoteProfileSource(source: RemoteProfileSource): IRemoteProfileSource

    @Binds
    @Local
    fun bindLocalRecipeEncryptionSource(source: LocalEncryptionSource): IEncryptionSource

    @Binds
    @Remote
    fun bindRemoteRecipeEncryptionSource(source: RemoteEncryptionSource): IEncryptionSource

    @Binds
    @Local
    fun bindLocalFileSource(source: LocalFileSource): IFileSource

    @Binds
    @Remote
    fun bindRemoteFileSource(source: RemoteFileSource): IFileSource

    @Binds
    @Local
    fun bindLocalRecipeSource(source: LocalRecipeSource): ILocalRecipeSource

    @Binds
    @Remote
    fun bindRemoteRecipeSource(source: RemoteRecipeSource): IRemoteRecipeSource

    @Binds
    @Local
    fun bindLocalRecipePictureSource(source: LocalRecipePictureSource): IRecipePictureSource

    @Binds
    @Remote
    fun bindRemoteRecipePictureSource(source: RemoteRecipePictureSource): IRecipePictureSource

    @Binds
    @Local
    fun bindLocalRecipeInteractionSource(source: LocalRecipeInteractionSource): ILocalRecipeInteractionSource

    @Binds
    @Remote
    fun bindRemoteRecipeInteractionSource(source: RemoteRecipeInteractionSource): IRecipeInteractionSource

    @Binds
    @Local
    fun bindLocalCategorySource(source: LocalCategorySource): ILocalCategorySource

    @Binds
    @Remote
    fun bindRemoteCategorySource(source: RemoteCategorySource): IRemoteCategorySource

    @Binds
    @Local
    fun bindLocalShoppingListSource(source: LocalShoppingListSource): IShoppingListSource

    @Binds
    @Remote
    fun bindRemoteShoppingListSource(source: RemoteShoppingListSource): IShoppingListSource

}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Local

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote
