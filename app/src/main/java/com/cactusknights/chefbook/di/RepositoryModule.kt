package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.domain.*
import com.cactusknights.chefbook.data.repositories.*
import com.cactusknights.chefbook.data.repositories.recipes.RecipeCrudRepoImpl
import com.cactusknights.chefbook.data.repositories.sync.RecipeBookRepoImpl
import com.cactusknights.chefbook.data.repositories.recipes.RecipeInteractionRepoImpl
import com.cactusknights.chefbook.data.repositories.recipes.RecipePicturesRepoImpl
import com.cactusknights.chefbook.data.repositories.sync.CategoriesSyncRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindAuthRepo(repo: AuthRepoImpl): AuthRepo

    @Binds
    fun bindProfileRepo(repo: ProfileRepoImpl): ProfileRepo

    @Binds
    fun bindRecipeBookSyncRepo(repo: RecipeBookRepoImpl): RecipeBookSyncRepo

    @Binds
    fun bindRecipeCrudRepo(repo: RecipeCrudRepoImpl): RecipeCrudRepo

    @Binds
    fun bindRecipePicturesRepo(repo: RecipePicturesRepoImpl): RecipePicturesRepo

    @Binds
    fun bindRecipeInteractionRepo(repo: RecipeInteractionRepoImpl): RecipeInteractionRepo

    @Binds
    fun bindCategoriesSyncRepo(repo: CategoriesSyncRepoImpl): CategoriesSyncRepo

    @Binds
    fun bindCategoriesCrudRepo(repo: CategoriesCrudRepoImpl): CategoriesCrudRepo

    @Binds
    fun bindShoppingListRepo(repo: ShoppingListRepoImpl): ShoppingListRepo

    @Binds
    fun bindVaultEncryptionRepo(repo: EncryptionRepoImpl): VaultEncryptionRepo

    @Binds
    fun bindRecipeEncryptionRepo(repo: EncryptionRepoImpl): RecipeEncryptionRepo
}