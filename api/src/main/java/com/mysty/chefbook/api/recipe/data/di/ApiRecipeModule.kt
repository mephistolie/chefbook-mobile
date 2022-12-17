package com.mysty.chefbook.api.recipe.data.di

import com.mysty.chefbook.api.recipe.data.cache.IRecipeBookCache
import com.mysty.chefbook.api.recipe.data.cache.IRecipeBookCacheReader
import com.mysty.chefbook.api.recipe.data.cache.IRecipeBookCacheWriter
import com.mysty.chefbook.api.recipe.data.cache.RecipeBookCache
import com.mysty.chefbook.api.recipe.data.crypto.RecipeCryptor
import com.mysty.chefbook.api.recipe.data.local.DataStoreUtils
import com.mysty.chefbook.api.recipe.data.local.LocalRecipeInteractionSource
import com.mysty.chefbook.api.recipe.data.local.LocalRecipePictureSource
import com.mysty.chefbook.api.recipe.data.local.LocalRecipeSource
import com.mysty.chefbook.api.recipe.data.remote.RemoteRecipeInteractionSource
import com.mysty.chefbook.api.recipe.data.remote.RemoteRecipePictureSource
import com.mysty.chefbook.api.recipe.data.remote.RemoteRecipeSource
import com.mysty.chefbook.api.recipe.data.remote.api.RecipeApi
import com.mysty.chefbook.api.recipe.data.repositories.ILocalRecipeInteractionSource
import com.mysty.chefbook.api.recipe.data.repositories.ILocalRecipeSource
import com.mysty.chefbook.api.recipe.data.repositories.IRecipePictureSource
import com.mysty.chefbook.api.recipe.data.repositories.IRemoteRecipeInteractionSource
import com.mysty.chefbook.api.recipe.data.repositories.IRemoteRecipeSource
import com.mysty.chefbook.api.recipe.data.repositories.LatestRecipesRepo
import com.mysty.chefbook.api.recipe.data.repositories.RecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.data.repositories.RecipeInteractionRepo
import com.mysty.chefbook.api.recipe.data.repositories.RecipePictureRepo
import com.mysty.chefbook.api.recipe.data.repositories.RecipeRepo
import com.mysty.chefbook.api.recipe.domain.ILatestRecipesRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeEncryptionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeInteractionRepo
import com.mysty.chefbook.api.recipe.domain.IRecipePictureRepo
import com.mysty.chefbook.api.recipe.domain.IRecipeRepo
import com.mysty.chefbook.api.recipe.domain.usecases.CreateRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.DecryptRecipeDataUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.DeleteRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.GetLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.GetRecipeBookUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.GetRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ICreateRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IDecryptRecipeDataUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IDeleteRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IGetLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeBookUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IGetRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IObserveRecipeBookUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeCategoriesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeFavouriteStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeLikeStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ISetRecipeSaveStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.IUpdateRecipeUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ObserveLatestRecipesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.ObserveRecipeBookUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.SetRecipeCategoriesUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.SetRecipeFavouriteStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.SetRecipeLikeStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.SetRecipeSaveStatusUseCase
import com.mysty.chefbook.api.recipe.domain.usecases.UpdateRecipeUseCase
import com.mysty.chefbook.api.sources.domain.IRecipeCryptor
import com.mysty.chefbook.core.di.Qualifiers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val apiRecipeModule = module {

    singleOf(::RecipeCryptor) bind IRecipeCryptor::class

    singleOf(::RecipeBookCache) binds arrayOf(IRecipeBookCache::class, IRecipeBookCacheReader::class, IRecipeBookCacheWriter::class)

    singleOf(DataStoreUtils::getLatestRecipesDataStore) { named(Qualifiers.DataStore.LATEST_RECIPES) }

    single { createRecipeApi(get(named(Qualifiers.AUTHORIZED))) }

    singleOf(::LocalRecipeSource) { named(Qualifiers.LOCAL) } bind ILocalRecipeSource::class
    singleOf(::RemoteRecipeSource) { named(Qualifiers.REMOTE) } bind IRemoteRecipeSource::class

    singleOf(::LocalRecipePictureSource) { named(Qualifiers.LOCAL) } bind IRecipePictureSource::class
    singleOf(::RemoteRecipePictureSource) { named(Qualifiers.REMOTE) } bind IRecipePictureSource::class

    singleOf(::LocalRecipeInteractionSource) { named(Qualifiers.LOCAL) } bind ILocalRecipeInteractionSource::class
    singleOf(::RemoteRecipeInteractionSource) { named(Qualifiers.REMOTE) } bind IRemoteRecipeInteractionSource::class

    single<IRecipeRepo> { RecipeRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(named(Qualifiers.LOCAL)),
        get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single<IRecipePictureRepo> { RecipePictureRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get(), get()) }
    single<IRecipeInteractionRepo> { RecipeInteractionRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }
    single<IRecipeEncryptionRepo> { RecipeEncryptionRepo(get(named(Qualifiers.LOCAL)), get(named(Qualifiers.REMOTE)), get(), get()) }
    single<ILatestRecipesRepo> { LatestRecipesRepo(get(named(Qualifiers.DataStore.LATEST_RECIPES))) }

    factoryOf(::ObserveRecipeBookUseCase) bind IObserveRecipeBookUseCase::class
    factoryOf(::ObserveLatestRecipesUseCase) bind IObserveLatestRecipesUseCase::class
    factoryOf(::GetRecipeBookUseCase) bind IGetRecipeBookUseCase::class
    factoryOf(::GetLatestRecipesUseCase) bind IGetLatestRecipesUseCase::class
    factoryOf(::GetRecipeUseCase) bind IGetRecipeUseCase::class
    factoryOf(::CreateRecipeUseCase) bind ICreateRecipeUseCase::class
    factoryOf(::UpdateRecipeUseCase) bind IUpdateRecipeUseCase::class
    factoryOf(::DeleteRecipeUseCase) bind IDeleteRecipeUseCase::class
    factoryOf(::DecryptRecipeDataUseCase) bind IDecryptRecipeDataUseCase::class
    factoryOf(::SetRecipeLikeStatusUseCase) bind ISetRecipeLikeStatusUseCase::class
    factoryOf(::SetRecipeSaveStatusUseCase) bind ISetRecipeSaveStatusUseCase::class
    factoryOf(::SetRecipeFavouriteStatusUseCase) bind ISetRecipeFavouriteStatusUseCase::class
    factoryOf(::SetRecipeCategoriesUseCase) bind ISetRecipeCategoriesUseCase::class

}

private fun createRecipeApi(retrofit: Retrofit) = retrofit.create(RecipeApi::class.java)
