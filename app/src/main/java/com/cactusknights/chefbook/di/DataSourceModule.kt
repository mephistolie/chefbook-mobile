package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.*
import com.cactusknights.chefbook.data.sources.local.datasources.LocalCategoriesDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.LocalEncryptionDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.LocalShoppingListDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.LocalProfileDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.recipes.LocalRecipeBookDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.recipes.LocalRecipeInteractionDataSourceImpl
import com.cactusknights.chefbook.data.sources.local.datasources.recipes.LocalRecipePicturesDataSourceImpl
import com.cactusknights.chefbook.data.sources.remote.datasources.*
import com.cactusknights.chefbook.data.sources.remote.datasources.recipes.RemoteRecipeBookDataSourceImpl
import com.cactusknights.chefbook.data.sources.remote.datasources.recipes.RemoteRecipeInteractionDataSourceImpl
import com.cactusknights.chefbook.data.sources.remote.datasources.recipes.RemoteRecipePicturesDataSourceImpl
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
    fun bindRemoteAuthDataSource(source: RemoteAuthDataSourceImpl): AuthDataSource

    @Binds
    @Local
    fun bindLocalUsersRepo(source: LocalProfileDataSourceImpl): LocalProfileDataSource

    @Binds
    @Remote
    fun bindRemoteUsersRepo(source: RemoteProfileDataSourceImpl): RemoteProfileDataSource

    @Binds
    @Local
    fun bindLocalRecipeEncryptionDataSource(source: LocalEncryptionDataSourceImpl): EncryptionDataSource

    @Binds
    @Remote
    fun bindRemoteRecipeEncryptionDataSource(source: RemoteEncryptionDataSourceImpl): EncryptionDataSource

    @Binds
    @Local
    fun bindLocalRecipesDataSource(source: LocalRecipeBookDataSourceImpl): LocalRecipeBookDataSource

    @Binds
    @Remote
    fun bindRemoteRecipesDataSource(source: RemoteRecipeBookDataSourceImpl): RecipeCrudDataSource

    @Binds
    @Local
    fun bindLocalRecipePicturesDataSource(source: LocalRecipePicturesDataSourceImpl): RecipePicturesDataSource

    @Binds
    @Remote
    fun bindRemoteRecipePicturesDataSource(source: RemoteRecipePicturesDataSourceImpl): RecipePicturesDataSource

    @Binds
    @Local
    fun bindLocalRecipeInteractionDataSource(source: LocalRecipeInteractionDataSourceImpl): LocalRecipeInteractionDataSource

    @Binds
    @Remote
    fun bindRemoteRecipeInteractionDataSource(source: RemoteRecipeInteractionDataSourceImpl): RecipeInteractionDataSource

    @Binds
    @Local
    fun bindLocalRecipeCategoriesDataSource(source: LocalCategoriesDataSourceImpl): LocalCategoriesDataSource

    @Binds
    @Remote
    fun bindRemoteRecipeCategoriesDataSource(source: RemoteCategoriesDataSourceImpl): CategoriesDataSource

    @Binds
    @Local
    fun bindLocalRecipeShoppingListDataSource(source: LocalShoppingListDataSourceImpl): ShoppingListDataSource

    @Binds
    @Remote
    fun bindRemoteRecipeShoppingListDataSource(source: RemoteShoppingListDataSourceImpl): ShoppingListDataSource
}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Local

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Remote