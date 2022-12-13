package com.cactusknights.chefbook.di

import com.cactusknights.chefbook.domain.usecases.auth.ChooseLocalModeUseCase
import com.cactusknights.chefbook.domain.usecases.auth.IChooseLocalModeUseCase
import com.cactusknights.chefbook.domain.usecases.auth.ISignInUseCase
import com.cactusknights.chefbook.domain.usecases.auth.ISignUpUseCase
import com.cactusknights.chefbook.domain.usecases.auth.SignInUseCase
import com.cactusknights.chefbook.domain.usecases.auth.SignUpUseCase
import com.cactusknights.chefbook.domain.usecases.category.CreateCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.DeleteCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.GetCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.category.GetCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.ICreateCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.IDeleteCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.IGetCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.category.IGetCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.IObserveCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.category.IUpdateCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.category.ObserveCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.category.UpdateCategoryUseCase
import com.cactusknights.chefbook.domain.usecases.common.IRefreshDataUseCase
import com.cactusknights.chefbook.domain.usecases.common.RefreshDataUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.CreateEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.DeleteEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.GetEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.ICreateEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.IDeleteEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.IGetEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.ILockEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.IObserveEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.IUnlockEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.LockEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.ObserveEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.UnlockEncryptedVaultUseCase
import com.cactusknights.chefbook.domain.usecases.profile.IObserveProfileUseCase
import com.cactusknights.chefbook.domain.usecases.profile.ObserveProfileUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.CreateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.DecryptRecipeDataUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.DeleteRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetLatestRecipesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeAsTextUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeBookUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ICreateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IDecryptRecipeDataUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IDeleteRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetLatestRecipesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeAsTextUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeBookUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IGetRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IObserveLatestRecipesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IObserveRecipeBookUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeFavouriteStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeLikeStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ISetRecipeSaveStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.IUpdateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ObserveLatestRecipesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ObserveRecipeBookUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.SetRecipeCategoriesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.SetRecipeFavouriteStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.SetRecipeLikeStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.SetRecipeSaveStatusUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.UpdateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.settings.IObserveSettingsUseCase
import com.cactusknights.chefbook.domain.usecases.settings.ISetDefaultRecipeLanguageUseCase
import com.cactusknights.chefbook.domain.usecases.settings.ObserveSettingsUseCase
import com.cactusknights.chefbook.domain.usecases.settings.SetDefaultRecipeLanguageUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.AddToShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.GetShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.IAddToShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.IGetShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.IObserveShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.IRemovePurchasedItemsUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.ISetShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.ISwitchPurchaseStatusUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.ISyncShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.ObserveShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.RemovePurchasedItemsUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.SetShoppingListUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.SwitchPurchaseStatusUseCase
import com.cactusknights.chefbook.domain.usecases.shopinglist.SyncShoppingListUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCasesModule = module {
    factoryOf(::ObserveSettingsUseCase) bind IObserveSettingsUseCase::class
    factoryOf(::SetDefaultRecipeLanguageUseCase) bind ISetDefaultRecipeLanguageUseCase::class
    
    factoryOf(::RefreshDataUseCase) bind IRefreshDataUseCase::class
    
    factoryOf(::SignUpUseCase) bind ISignUpUseCase::class
    factoryOf(::SignInUseCase) bind ISignInUseCase::class
    factoryOf(::ChooseLocalModeUseCase) bind IChooseLocalModeUseCase::class
    
    factoryOf(::ObserveProfileUseCase) bind IObserveProfileUseCase::class
    
    factoryOf(::ObserveEncryptedVaultStateUseCase) bind IObserveEncryptedVaultStateUseCase::class
    factoryOf(::GetEncryptedVaultStateUseCase) bind IGetEncryptedVaultStateUseCase::class
    factoryOf(::CreateEncryptedVaultUseCase) bind ICreateEncryptedVaultUseCase::class
    factoryOf(::UnlockEncryptedVaultUseCase) bind IUnlockEncryptedVaultUseCase::class
    factoryOf(::LockEncryptedVaultUseCase) bind ILockEncryptedVaultUseCase::class
    factoryOf(::DeleteEncryptedVaultUseCase) bind IDeleteEncryptedVaultUseCase::class
        
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
    factoryOf(::GetRecipeAsTextUseCase) bind IGetRecipeAsTextUseCase::class
    
    factoryOf(::ObserveCategoriesUseCase) bind IObserveCategoriesUseCase::class
    factoryOf(::GetCategoriesUseCase) bind IGetCategoriesUseCase::class
    factoryOf(::GetCategoryUseCase) bind IGetCategoryUseCase::class
    factoryOf(::CreateCategoryUseCase) bind ICreateCategoryUseCase::class
    factoryOf(::UpdateCategoryUseCase) bind IUpdateCategoryUseCase::class
    factoryOf(::DeleteCategoryUseCase) bind IDeleteCategoryUseCase::class
    
    factoryOf(::ObserveShoppingListUseCase) bind IObserveShoppingListUseCase::class
    factoryOf(::GetShoppingListUseCase) bind IGetShoppingListUseCase::class
    factoryOf(::SyncShoppingListUseCase) bind ISyncShoppingListUseCase::class
    factoryOf(::RemovePurchasedItemsUseCase) bind IRemovePurchasedItemsUseCase::class
    factoryOf(::SwitchPurchaseStatusUseCase) bind ISwitchPurchaseStatusUseCase::class
    factoryOf(::SetShoppingListUseCase) bind ISetShoppingListUseCase::class
    factoryOf(::AddToShoppingListUseCase) bind IAddToShoppingListUseCase::class
}
