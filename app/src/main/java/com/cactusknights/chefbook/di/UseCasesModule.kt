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
import com.cactusknights.chefbook.domain.usecases.encryption.IObserveEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.encryption.ObserveEncryptedVaultStateUseCase
import com.cactusknights.chefbook.domain.usecases.profile.IObserveProfileUseCase
import com.cactusknights.chefbook.domain.usecases.profile.ObserveProfileUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.CreateRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.DeleteRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetLatestRecipesUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeAsTextUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeBookUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.GetRecipeUseCase
import com.cactusknights.chefbook.domain.usecases.recipe.ICreateRecipeUseCase
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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesBindingModule {

    @Binds
    fun bindObserveSettingsUseCase(useCase: ObserveSettingsUseCase): IObserveSettingsUseCase

    @Binds
    fun bindSetDefaultRecipeLanguageUseCase(useCase: SetDefaultRecipeLanguageUseCase): ISetDefaultRecipeLanguageUseCase

    @Binds
    fun bindRefreshDataUseCase(useCase: RefreshDataUseCase): IRefreshDataUseCase

    @Binds
    fun bindSignUpUseCase(useCase: SignUpUseCase): ISignUpUseCase

    @Binds
    fun bindSignInUseCase(useCase: SignInUseCase): ISignInUseCase

    @Binds
    fun bindChooseLocalModeUseCase(useCase: ChooseLocalModeUseCase): IChooseLocalModeUseCase

    @Binds
    fun bindObserveProfileUseCase(useCase: ObserveProfileUseCase): IObserveProfileUseCase

    @Binds
    fun bindObserveEncryptedVaultStateUseCase(useCase: ObserveEncryptedVaultStateUseCase): IObserveEncryptedVaultStateUseCase

    @Binds
    fun bindObserveRecipeBookUseCase(useCase: ObserveRecipeBookUseCase): IObserveRecipeBookUseCase

    @Binds
    fun bindGetRecipeBookUseCase(useCase: GetRecipeBookUseCase): IGetRecipeBookUseCase

    @Binds
    fun bindGetRecipeUseCase(useCase: GetRecipeUseCase): IGetRecipeUseCase

    @Binds
    fun bindCreateRecipeUseCase(useCase: CreateRecipeUseCase): ICreateRecipeUseCase

    @Binds
    fun bindUpdateRecipeUseCase(useCase: UpdateRecipeUseCase): IUpdateRecipeUseCase

    @Binds
    fun bindDeleteRecipeUseCase(useCase: DeleteRecipeUseCase): IDeleteRecipeUseCase

    @Binds
    fun binSetRecipeLikeStatusUseCase(useCase: SetRecipeLikeStatusUseCase): ISetRecipeLikeStatusUseCase

    @Binds
    fun bindSetRecipeSaveStatusUseCase(useCase: SetRecipeSaveStatusUseCase): ISetRecipeSaveStatusUseCase

    @Binds
    fun bindSetRecipeFavouriteStatusUseCase(useCase: SetRecipeFavouriteStatusUseCase): ISetRecipeFavouriteStatusUseCase

    @Binds
    fun bindSetRecipeCategoriesUseCase(useCase: SetRecipeCategoriesUseCase): ISetRecipeCategoriesUseCase

    @Binds
    fun bindGetRecipeAsTextUseCase(useCase: GetRecipeAsTextUseCase): IGetRecipeAsTextUseCase

    @Binds
    fun bindObserveLatestRecipesUseCase(useCase: ObserveLatestRecipesUseCase): IObserveLatestRecipesUseCase

    @Binds
    fun bindGetLatestRecipesUseCase(useCase: GetLatestRecipesUseCase): IGetLatestRecipesUseCase

    @Binds
    fun bindObserveCategoriesUseCase(useCase: ObserveCategoriesUseCase): IObserveCategoriesUseCase

    @Binds
    fun bindGetCategoriesUseCase(useCase: GetCategoriesUseCase): IGetCategoriesUseCase

    @Binds
    fun bindGetCategoryUseCase(useCase: GetCategoryUseCase): IGetCategoryUseCase

    @Binds
    fun bindCreateCategoryUseCase(useCase: CreateCategoryUseCase): ICreateCategoryUseCase

    @Binds
    fun bindUpdateCategoryUseCase(useCase: UpdateCategoryUseCase): IUpdateCategoryUseCase

    @Binds
    fun bindDeleteCategoryUseCase(useCase: DeleteCategoryUseCase): IDeleteCategoryUseCase

}