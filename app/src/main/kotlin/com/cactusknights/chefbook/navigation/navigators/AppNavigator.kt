package com.cactusknights.chefbook.navigation.navigators

import androidx.navigation.NavHostController
import com.cactusknights.chefbook.BuildConfig
import com.cactusknights.chefbook.navigation.graphs.NavGraphs
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.mysty.chefbook.core.android.compose.providers.ContentType
import com.mysty.chefbook.features.about.ui.destinations.AboutScreenDestination
import com.mysty.chefbook.features.auth.ui.navigation.IAuthScreenNavigator
import com.mysty.chefbook.features.category.ui.input.destinations.CategoryInputDialogDestination
import com.mysty.chefbook.features.encryption.ui.vault.destinations.EncryptedVaultScreenDestination
import com.mysty.chefbook.features.home.ui.destinations.HomeScreenDestination
import com.mysty.chefbook.features.home.ui.navigation.IHomeScreenNavigator
import com.mysty.chefbook.features.purchases.input.ui.destinations.PurchaseInputDialogDestination
import com.mysty.chefbook.features.recipe.control.ui.navigation.IRecipeControlScreenNavigator
import com.mysty.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
import com.mysty.chefbook.features.recipe.info.ui.navigation.IRecipeScreenNavigator
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputDetailsScreenNavigator
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputIngredientsScreenNavigator
import com.mysty.chefbook.features.recipe.input.navigation.IRecipeInputScreenBaseNavigator
import com.mysty.chefbook.features.recipe.input.ui.destinations.CaloriesDialogDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.EncryptionStateDialogDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.IngredientDialogDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.LanguageDialogDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.RecipeInputCookingScreenDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.RecipeInputDetailsScreenDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.RecipeInputIngredientScreenDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.RecipeSavedDialogDestination
import com.mysty.chefbook.features.recipe.input.ui.destinations.VisibilityDialogDestination
import com.mysty.chefbook.features.recipe.share.ui.destinations.RecipeShareDialogDestination
import com.mysty.chefbook.features.recipebook.category.ui.destinations.CategoryRecipesScreenDestination
import com.mysty.chefbook.features.recipebook.category.ui.navigation.ICategoryRecipesScreenNavigator
import com.mysty.chefbook.features.recipebook.dashboard.ui.navigation.IRecipeBookScreenNavigator
import com.mysty.chefbook.features.recipebook.favourite.ui.destinations.FavouriteRecipesScreenDestination
import com.mysty.chefbook.features.recipebook.favourite.ui.navigation.IRecipeBookFavouriteScreenNavigator
import com.mysty.chefbook.features.recipebook.search.ui.destinations.RecipeBookSearchScreenDestination
import com.mysty.chefbook.features.recipebook.search.ui.navigation.IRecipeBookSearchScreenNavigator
import com.mysty.chefbook.features.shoppinglist.control.ui.screen.navigation.IShoppingListScreenNavigator
import com.mysty.chefbook.navigation.navigators.IBaseNavigator
import com.mysty.chefbook.navigation.navigators.IDialogNavigator
import com.mysty.chefbook.navigation.params.dialogs.OneButtonDialogParams
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import com.mysty.chefbook.ui.common.dialogs.destinations.DismissibleOneButtonDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.NonDismissibleOneButtonDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.NonDismissibleTwoButtonsDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.PicturesViewerDestination
import com.mysty.chefbook.ui.common.dialogs.utils.ErrorUtils
import com.mysty.chefbook.ui.common.presentation.RecipeScreenPage
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack

@OptIn(ExperimentalMaterialNavigationApi::class)
class AppNavigator constructor(
  val navController: NavHostController,
  val bottomSheet: BottomSheetNavigator,
  val hideBottomSheet: () -> Boolean,
) : IBaseNavigator,
  IDialogNavigator,
  IHomeScreenNavigator,
  IAuthScreenNavigator,
  IShoppingListScreenNavigator,
  IRecipeBookScreenNavigator,
  IRecipeBookSearchScreenNavigator,
  IRecipeBookFavouriteScreenNavigator,
  ICategoryRecipesScreenNavigator,
  IRecipeScreenNavigator,
  IRecipeControlScreenNavigator,
  IRecipeInputScreenBaseNavigator,
  IRecipeInputDetailsScreenNavigator,
  IRecipeInputIngredientsScreenNavigator {

  override fun openOneButtonDialog(params: OneButtonDialogParams, request: String) {
    val destination = if (params.nonDismissible) {
      NonDismissibleOneButtonDialogDestination(params = params, request = request)
    } else {
      DismissibleOneButtonDialogDestination(params = params, request = request)
    }
    navController.navigate(destination)
  }

  override fun openTwoButtonsDialog(params: TwoButtonsDialogParams, request: String) {
    val destination = if (params.nonDismissible) {
      NonDismissibleTwoButtonsDialogDestination(params = params, request = request)
    } else {
      DismissibleTwoButtonsDialogDestination(params = params, request = request)
    }
    navController.navigate(destination)
  }

  override fun openErrorInfoDialog(
    error: Throwable?,
    dialogParams: OneButtonDialogParams,
    request: String
  ) {
    val processedParams = dialogParams.copy(
      titleId = ErrorUtils.getDialogTitleId(error),
      descriptionId = dialogParams.descriptionId ?: ErrorUtils.getDialogDescriptionId(error)
    )
    openOneButtonDialog(params = processedParams, request = request)
  }

  override fun openErrorDialog(
    error: Throwable?,
    dialogParams: TwoButtonsDialogParams,
    request: String
  ) {
    val processedParams = dialogParams.copy(
      titleId = ErrorUtils.getDialogTitleId(error),
      descriptionId = dialogParams.descriptionId ?: ErrorUtils.getDialogDescriptionId(error)
    )
    openTwoButtonsDialog(params = processedParams, request = request)
  }

  override fun openEncryptedVaultScreen() {
    navController.navigate(EncryptedVaultScreenDestination())
  }

  override fun openAboutScreen() {
    navController.navigate(AboutScreenDestination(versionName = BuildConfig.VERSION_NAME))
  }

  override fun openHomeScreen() {
    navController.navigate(HomeScreenDestination) {
      popUpTo(HomeScreenDestination.route) { inclusive = true }
    }
  }

  override fun openRecipeBookSearchScreen() {
    navController.navigate(RecipeBookSearchScreenDestination)
  }

  override fun openFavouriteRecipesScreen() {
    navController.navigate(FavouriteRecipesScreenDestination)
  }

  override fun openCategoryRecipesScreen(categoryId: String) {
    if (!navController.popBackStack(
        route = CategoryRecipesScreenDestination.route,
        inclusive = false
      )
    ) {
      navController.navigate(CategoryRecipesScreenDestination(categoryId = categoryId))
    }
  }

  override fun openRecipeShareDialog(recipeId: String) {
    navController.navigate(RecipeShareDialogDestination(recipeId = recipeId))
  }

  override fun openPicturesViewer(
    pictures: Array<String>,
    startIndex: Int,
    picturesType: ContentType,
  ) {
    navController.navigate(
      PicturesViewerDestination(
        pictures = pictures,
        startIndex = startIndex,
        picturesType = picturesType
      )
    )
  }

  override fun closeRecipeScreen() {
    navController.popBackStack(RecipeScreenDestination, inclusive = true)
  }

  override fun openRecipeScreen(recipeId: String) {
    this.openRecipeScreen(
      recipeId = recipeId,
      initPage = RecipeScreenPage.INGREDIENTS,
      openExpanded = false,
    )
  }

  override fun openRecipeScreen(
    recipeId: String,
    initPage: RecipeScreenPage,
    openExpanded: Boolean,
  ) {
    navController.navigate(
      RecipeScreenDestination(
        recipeId = recipeId,
        initPage = initPage,
        openExpanded = openExpanded
      )
    )
  }

  override fun openRecipeInputScreen() = openRecipeInputScreen(recipeId = null)
  override fun openRecipeInputScreen(recipeId: String?) {
    navController.navigate(NavGraphs.recipeInput(recipeId = recipeId))
  }

  override fun openRecipeInputDetailsScreen() {
    navController.navigate(RecipeInputDetailsScreenDestination)
  }

  override fun openVisibilityDialog() {
    navController.navigate(VisibilityDialogDestination)
  }

  override fun openLanguageDialog() {
    navController.navigate(LanguageDialogDestination)
  }

  override fun openEncryptionStatePickerDialog() {
    navController.navigate(EncryptionStateDialogDestination)
  }

  override fun openCaloriesDialog() {
    navController.navigate(CaloriesDialogDestination)
  }

  override fun openRecipeInputIngredientScreen() {
    navController.navigate(RecipeInputIngredientScreenDestination)
  }

  override fun openIngredientDialog(ingredientId: String) {
    navController.navigate(IngredientDialogDestination(ingredientId))
  }

  override fun openRecipeInputCookingScreen() {
    navController.navigate(RecipeInputCookingScreenDestination)
  }

  override fun openSavedDialog() {
    navController.navigate(RecipeSavedDialogDestination)
  }

  override fun closeRecipeInput(recipeId: String?) {
    navController.popBackStack(NavGraphs.recipeInput.route, inclusive = true)
    if (recipeId != null &&
      navController.currentDestination?.route != RecipeScreenDestination(recipeId).route
    ) {
      openRecipeScreen(recipeId)
    }
  }

  override fun openCategoryInputDialog() = openCategoryInputDialog(categoryId = null)
  override fun openCategoryInputDialog(categoryId: String?) {
    navController.navigate(CategoryInputDialogDestination(categoryId = categoryId))
  }

  override fun openPurchaseInput(purchaseId: String) {
    navController.navigate(PurchaseInputDialogDestination(purchaseId))
  }

  override fun navigateUp(skipAnimation: Boolean) {
    if (skipAnimation || !hideBottomSheet()) navController.navigateUp()
  }

}
