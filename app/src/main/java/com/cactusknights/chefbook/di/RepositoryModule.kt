package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.data.repositories.AuthRepo
import com.cactusknights.chefbook.data.repositories.CategoryRepo
import com.cactusknights.chefbook.data.repositories.EncryptedVaultRepo
import com.cactusknights.chefbook.data.repositories.FileRepo
import com.cactusknights.chefbook.data.repositories.IFileRepo
import com.cactusknights.chefbook.data.repositories.ProfileRepo
import com.cactusknights.chefbook.data.repositories.SessionRepo
import com.cactusknights.chefbook.data.repositories.SettingsRepo
import com.cactusknights.chefbook.data.repositories.ShoppingListRepo
import com.cactusknights.chefbook.data.repositories.SourceRepo
import com.cactusknights.chefbook.data.repositories.recipes.LatestRecipesRepo
import com.cactusknights.chefbook.data.repositories.recipes.RecipeEncryptionRepo
import com.cactusknights.chefbook.data.repositories.recipes.RecipeInteractionRepo
import com.cactusknights.chefbook.data.repositories.recipes.RecipePictureRepo
import com.cactusknights.chefbook.data.repositories.recipes.RecipeRepo
import com.cactusknights.chefbook.domain.interfaces.IAuthRepo
import com.cactusknights.chefbook.domain.interfaces.ICategoryRepo
import com.cactusknights.chefbook.domain.interfaces.IEncryptedVaultRepo
import com.cactusknights.chefbook.domain.interfaces.ILatestRecipesRepo
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeEncryptionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeInteractionRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipePictureRepo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo
import com.cactusknights.chefbook.domain.interfaces.ISessionRepo
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import com.cactusknights.chefbook.domain.interfaces.IShoppingListRepo
import com.cactusknights.chefbook.domain.interfaces.ISourceRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindAuthRepo(repo: AuthRepo): IAuthRepo

    @Binds
    fun bindProfileRepo(repo: ProfileRepo): IProfileRepo

    @Binds
    fun bindSettingsRepo(repo: SettingsRepo): ISettingsRepo

    @Binds
    fun bindSourceRepo(repo: SourceRepo): ISourceRepo

    @Binds
    fun bindSessionRepo(repo: SessionRepo): ISessionRepo

    @Binds
    fun bindEncryptedVaultRepo(repo: EncryptedVaultRepo): IEncryptedVaultRepo

    @Binds
    fun bindFileRepo(repo: FileRepo): IFileRepo

    @Binds
    fun bindRecipeRepo(repo: RecipeRepo): IRecipeRepo

    @Binds
    fun bindRecipePictureRepo(repo: RecipePictureRepo): IRecipePictureRepo

    @Binds
    fun bindRecipeInteractionRepo(repo: RecipeInteractionRepo): IRecipeInteractionRepo

    @Binds
    fun bindRecipeEncryptionRepo(repo: RecipeEncryptionRepo): IRecipeEncryptionRepo

    @Binds
    fun bindLatestRecipesRepo(repo: LatestRecipesRepo): ILatestRecipesRepo

    @Binds
    fun bindCategoryRepo(repo: CategoryRepo): ICategoryRepo

    @Binds
    fun bindShoppingListRepo(repo: ShoppingListRepo): IShoppingListRepo

}
