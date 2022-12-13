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
import com.cactusknights.chefbook.data.repositories.recipe.LatestRecipesRepo
import com.cactusknights.chefbook.data.repositories.recipe.RecipeEncryptionRepo
import com.cactusknights.chefbook.data.repositories.recipe.RecipeInteractionRepo
import com.cactusknights.chefbook.data.repositories.recipe.RecipePictureRepo
import com.cactusknights.chefbook.data.repositories.recipe.RecipeRepo
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
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<IAuthRepo> { AuthRepo(get(named(Qualifiers.REMOTE))) }
    single<IProfileRepo> { ProfileRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }
    single<ISettingsRepo> { SettingsRepo(get(named(Qualifiers.DataStore.SETTINGS))) }
    single<ISourceRepo> { SourceRepo(get(), get(), get()) }
    single<ISessionRepo> { SessionRepo(get(named(Qualifiers.DataStore.TOKENS)), get()) }
    single<IEncryptedVaultRepo> { EncryptedVaultRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }
    single<IFileRepo> { FileRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE))) }
    single<IRecipeRepo> { RecipeRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(named(Qualifiers.LOCAL)),
        get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single<IRecipePictureRepo> { RecipePictureRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }
    single<IRecipeInteractionRepo> { RecipeInteractionRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }
    single<IRecipeEncryptionRepo> { RecipeEncryptionRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }
    single<ILatestRecipesRepo> { LatestRecipesRepo(get(named(Qualifiers.DataStore.LATEST_RECIPES))) }
    single<ICategoryRepo> { CategoryRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }
    single<IShoppingListRepo> { ShoppingListRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }

}
