package io.chefbook.navigation.navigators

import android.app.Activity
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import io.chefbook.core.android.compose.providers.ContentType
import io.chefbook.features.about.ui.destinations.AboutScreenDestination
import io.chefbook.features.auth.navigation.AuthScreenNavigator
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.features.category.ui.input.destinations.CategoryInputDialogDestination
import io.chefbook.features.community.languages.ui.destinations.CommunityLanguagesScreenDestination
import io.chefbook.features.community.recipes.navigation.CommunityRecipesFilterScreenNavigator
import io.chefbook.features.community.recipes.navigation.CommunityRecipesScreenNavigator
import io.chefbook.features.encryption.ui.vault.destinations.EncryptedVaultScreenDestination
import io.chefbook.features.profile.control.navigation.ProfileScreenNavigator
import io.chefbook.features.profile.control.ui.destinations.ProfileScreenDestination
import io.chefbook.features.profile.editing.ui.destinations.ProfileEditingScreenDestination
import io.chefbook.features.recipe.control.navigation.RecipeControlScreenNavigator
import io.chefbook.features.recipe.info.navigation.RecipeScreenNavigator
import io.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
import io.chefbook.features.recipe.input.navigation.RecipeInputDetailsScreenNavigator
import io.chefbook.features.recipe.input.navigation.RecipeInputIngredientsScreenNavigator
import io.chefbook.features.recipe.input.navigation.RecipeInputScreenBaseNavigator
import io.chefbook.features.recipe.input.ui.destinations.CaloriesDialogDestination
import io.chefbook.features.recipe.input.ui.destinations.EncryptionStateDialogDestination
import io.chefbook.features.recipe.input.ui.destinations.IngredientDialogDestination
import io.chefbook.features.recipe.input.ui.destinations.LanguageDialogDestination
import io.chefbook.features.recipe.input.ui.destinations.RecipeInputCookingScreenDestination
import io.chefbook.features.recipe.input.ui.destinations.RecipeInputDetailsScreenDestination
import io.chefbook.features.recipe.input.ui.destinations.RecipeInputIngredientScreenDestination
import io.chefbook.features.recipe.input.ui.destinations.RecipeSavedDialogDestination
import io.chefbook.features.recipe.input.ui.destinations.VisibilityDialogDestination
import io.chefbook.features.recipe.share.ui.destinations.RecipeShareDialogDestination
import io.chefbook.features.recipebook.category.ui.destinations.CategoryRecipesScreenDestination
import io.chefbook.features.recipebook.category.ui.navigation.CategoryRecipesScreenNavigator
import io.chefbook.features.community.recipes.ui.screens.destinations.CommunityRecipesFilterScreenDestination
import io.chefbook.features.community.recipes.ui.screens.destinations.CommunityRecipesTagGroupScreenDestination
import io.chefbook.features.recipebook.dashboard.ui.destinations.DashboardScreenDestination as RecipeBookDashboardScreenDestination
import io.chefbook.features.recipebook.dashboard.ui.navigation.DashboardScreenNavigator as RecipeBookDashboardScreenNavigator
import io.chefbook.features.recipebook.favourite.ui.destinations.FavouriteRecipesScreenDestination
import io.chefbook.features.recipebook.favourite.ui.navigation.RecipeBookFavouriteScreenNavigator
import io.chefbook.features.recipebook.search.ui.destinations.RecipeBookSearchScreenDestination
import io.chefbook.features.recipebook.search.ui.navigation.RecipeBookSearchScreenNavigator
import io.chefbook.features.settings.navigation.SettingsScreenNavigator
import io.chefbook.features.settings.ui.destinations.SettingsScreenDestination
import io.chefbook.features.shoppinglist.control.navigation.ShoppingListScreenNavigator
import io.chefbook.features.shoppinglist.control.ui.screen.destinations.ShoppingListScreenDestination
import io.chefbook.features.shoppinglist.purchases.input.ui.destinations.PurchaseInputDialogDestination
import io.chefbook.navigation.graphs.NavGraphs
import io.chefbook.navigation.params.dialogs.OneButtonDialogParams
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.ui.MainActivity
import io.chefbook.ui.common.dialogs.destinations.DismissibleOneButtonDialogDestination
import io.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import io.chefbook.ui.common.dialogs.destinations.NonDismissibleOneButtonDialogDestination
import io.chefbook.ui.common.dialogs.destinations.NonDismissibleTwoButtonsDialogDestination
import io.chefbook.ui.common.dialogs.destinations.PicturesViewerDestination
import io.chefbook.ui.common.dialogs.utils.ErrorUtils
import io.chefbook.ui.common.presentation.RecipeScreenPage


@OptIn(ExperimentalMaterialNavigationApi::class)
class AppNavigator(
  val navController: NavHostController,
  val bottomSheet: BottomSheetNavigator,
  val hideBottomSheet: () -> Boolean,
) : BaseNavigator,
  DialogNavigator,
  SettingsScreenNavigator,
  AuthScreenNavigator,
  ShoppingListScreenNavigator,
  RecipeBookDashboardScreenNavigator,
  RecipeBookSearchScreenNavigator,
  RecipeBookFavouriteScreenNavigator,
  CategoryRecipesScreenNavigator,
  RecipeScreenNavigator,
  RecipeControlScreenNavigator,
  RecipeInputScreenBaseNavigator,
  RecipeInputDetailsScreenNavigator,
  RecipeInputIngredientsScreenNavigator,
  CommunityRecipesScreenNavigator,
  CommunityRecipesFilterScreenNavigator,
  ProfileScreenNavigator {

  @Composable
  override fun currentBackStackEntry() = navController.currentBackStackEntryAsState()

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

  fun openAuthScreen() {
    navController.navigate(AuthScreenDestination()) {
      navController.currentDestination?.route?.let { route ->
        popUpTo(route) { inclusive = true }
      }
    }
  }

  override fun openCommunityRecipesScreen() {
    navController.navigate(NavGraphs.communityRecipes())
  }

  override fun openCommunityLanguagesPickerScreen() {
    navController.navigate(CommunityLanguagesScreenDestination)
  }

  override fun openCommunityRecipeSearch(search: String) {
    navController.navigate(NavGraphs.communityRecipes(initialSearch = search))
  }

  override fun openCommunityRecipesFilterScreen(
    focusSearch: Boolean,
    scrollToTags: Boolean,
  ) {
    navController.navigate(
      CommunityRecipesFilterScreenDestination(
        focusSearch = focusSearch,
        scrollToTags = scrollToTags,
      )
    )
  }

  override fun openTagGroupScreen(groupId: String?) {
    navController.navigate(
      CommunityRecipesTagGroupScreenDestination(groupId = groupId)
    )
  }

  override fun openEncryptedVaultScreen() {
    navController.navigate(EncryptedVaultScreenDestination())
  }

  override fun openProfileScreen() {
    navController.navigate(ProfileScreenDestination)
  }

  override fun openProfileEditingScreen() {
    navController.navigate(ProfileEditingScreenDestination)
  }

  override fun openAppSettingsScreen() {
    navController.navigate(SettingsScreenDestination)
  }

  override fun openAboutAppScreen() {
    navController.navigate(AboutScreenDestination)
  }

  override fun openRecipeBookDashboardScreen() {
    navController.navigate(RecipeBookDashboardScreenDestination) {
      navController.currentDestination?.route?.let { route ->
        popUpTo(route) { inclusive = true }
      }
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

  override fun openPurchaseInput(shoppingListId: String, purchaseId: String) {
    navController.navigate(PurchaseInputDialogDestination(shoppingListId, purchaseId))
  }

  override fun openShoppingListScreen() {
    navController.navigate(ShoppingListScreenDestination)
  }

  override fun navigateUp(skipAnimation: Boolean) {
    if (skipAnimation || !hideBottomSheet()) navController.navigateUp()
  }

  override fun popBackStackToCurrent() {
    navController.currentDestination?.route?.let { route ->
      navController.popBackStack(route, false)
    }
  }

  override fun restartApp() {
    val context = navController.context
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    (context as? Activity)?.finish()
  }
}
