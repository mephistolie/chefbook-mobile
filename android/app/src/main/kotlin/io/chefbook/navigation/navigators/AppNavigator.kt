package io.chefbook.navigation.navigators

import android.app.Activity
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.spec.Direction
import io.chefbook.core.android.compose.providers.ContentType
import io.chefbook.features.about.ui.destinations.AboutScreenDestination
import io.chefbook.features.auth.navigation.AuthScreenNavigator
import io.chefbook.features.profile.editing.ui.navigation.ProfileEditingScreenNavigator
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.features.category.ui.input.destinations.CategoryInputScreenDestination
import io.chefbook.features.community.languages.ui.destinations.CommunityLanguagesScreenDestination
import io.chefbook.features.community.recipes.navigation.CommunityRecipesFilterScreenNavigator
import io.chefbook.features.community.recipes.navigation.CommunityRecipesScreenNavigator
import io.chefbook.features.community.recipes.ui.screens.destinations.CommunityRecipesContentScreenDestination
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
import io.chefbook.features.profile.deletion.ui.destinations.ProfileDeletionScreenDestination
import io.chefbook.features.recipebook.categories.ui.destinations.CategoriesScreenDestination
import io.chefbook.features.recipebook.creation.navigation.RecipeBookCreationScreenNavigator
import io.chefbook.features.recipebook.creation.ui.destinations.RecipeBookCreationScreenDestination
import io.chefbook.features.recipebook.dashboard.ui.destinations.DashboardScreenDestination as RecipeBookDashboardScreenDestination
import io.chefbook.features.recipebook.dashboard.ui.navigation.DashboardScreenNavigator as RecipeBookDashboardScreenNavigator
import io.chefbook.features.recipebook.favourite.ui.destinations.FavouriteRecipesScreenDestination
import io.chefbook.features.recipebook.categories.ui.navigation.CategoriesScreenNavigator
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


@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
class AppNavigator(
  val navController: NavHostController,
  val bottomSheet: BottomSheetNavigator,
  val hideBottomSheet: () -> Boolean,
) : BaseNavigator,
  DialogNavigator,
  SettingsScreenNavigator,
  AuthScreenNavigator,
  ProfileEditingScreenNavigator,
  ShoppingListScreenNavigator,
  RecipeBookDashboardScreenNavigator,
  RecipeBookFavouriteScreenNavigator,
  RecipeBookCreationScreenNavigator,
  RecipeBookSearchScreenNavigator,
  CategoriesScreenNavigator,
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
    navigateSafe(destination)
  }

  override fun openTwoButtonsDialog(params: TwoButtonsDialogParams, request: String) {
    val destination = if (params.nonDismissible) {
      NonDismissibleTwoButtonsDialogDestination(params = params, request = request)
    } else {
      DismissibleTwoButtonsDialogDestination(params = params, request = request)
    }
    navigateSafe(destination)
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
    navigateSafe(AuthScreenDestination()) {
      navController.currentDestination?.route?.let { route ->
        popUpTo(route) { inclusive = true }
      }
    }
  }

  override fun openCommunityRecipesScreen() {
    navigateSafe(CommunityRecipesContentScreenDestination)
  }

  override fun openCommunityLanguagesPickerScreen() {
    navigateSafe(CommunityLanguagesScreenDestination)
  }

  override fun openCommunityRecipeSearch(search: String) {
    navController.navigate(NavGraphs.communityRecipes(initialSearch = search))
  }

  override fun openCommunityRecipesFilterScreen(
    focusSearch: Boolean,
    scrollToTags: Boolean,
  ) {
    navigateSafe(
      CommunityRecipesFilterScreenDestination(
        focusSearch = focusSearch,
        scrollToTags = scrollToTags,
      )
    )
  }

  override fun openTagGroupScreen(groupId: String?) {
    navigateSafe(
      CommunityRecipesTagGroupScreenDestination(groupId = groupId)
    )
  }

  override fun openEncryptedVaultScreen() {
    navigateSafe(EncryptedVaultScreenDestination())
  }

  override fun openProfileScreen() {
    navigateSafe(ProfileScreenDestination)
  }

  override fun openProfileEditingScreen() {
    navigateSafe(ProfileEditingScreenDestination)
  }

  override fun openProfileDeletionScreen() {
    navigateSafe(ProfileDeletionScreenDestination)
  }

  override fun openAppSettingsScreen() {
    navigateSafe(SettingsScreenDestination)
  }

  override fun openAboutAppScreen() {
    navigateSafe(AboutScreenDestination)
  }

  override fun openRecipeBookDashboardScreen() {
    navigateSafe(RecipeBookDashboardScreenDestination) {
      navController.currentDestination?.route?.let { route ->
        popUpTo(route) { inclusive = true }
      }
    }
  }

  override fun openRecipeBookSearchScreen() {
    navigateSafe(RecipeBookSearchScreenDestination)
  }

  override fun openFavouriteRecipesScreen() {
    navigateSafe(FavouriteRecipesScreenDestination)
  }

  override fun openCategoriesScreen() {
    navigateSafe(CategoriesScreenDestination)
  }

  override fun openCategoryRecipesScreen(categoryId: String) {
    if (!navController.popBackStack(
        route = CategoryRecipesScreenDestination.route,
        inclusive = false
      )
    ) {
      navigateSafe(CategoryRecipesScreenDestination(
        categoryId = categoryId,
        isTag = false,
      ))
    }
  }

  override fun openTagRecipesScreen(tagId: String) {
    if (!navController.popBackStack(
        route = CategoryRecipesScreenDestination.route,
        inclusive = false
      )
    ) {
      navigateSafe(CategoryRecipesScreenDestination(
        categoryId = tagId,
        isTag = true,
      ))
    }
  }


  override fun openRecipeShareDialog(recipeId: String) {
    navigateSafe(RecipeShareDialogDestination(recipeId = recipeId))
  }

  override fun openPicturesViewer(
    pictures: Array<String>,
    startIndex: Int,
    picturesType: ContentType,
  ) {
    navigateSafe(
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
    navigateSafe(
      RecipeScreenDestination(
        recipeId = recipeId,
        initPage = initPage,
        openExpanded = openExpanded
      )
    )
  }

  override fun openRecipeBookCreationScreen() {
    navigateSafe(RecipeBookCreationScreenDestination)
  }

  override fun openRecipeInputScreen() = openRecipeInputScreen(recipeId = null)
  override fun openRecipeInputScreen(recipeId: String?) {
    navController.navigate(NavGraphs.recipeInput(recipeId = recipeId))
  }

  override fun openRecipeInputDetailsScreen() {
    navigateSafe(RecipeInputDetailsScreenDestination)
  }

  override fun openVisibilityDialog() {
    navigateSafe(VisibilityDialogDestination)
  }

  override fun openLanguageDialog() {
    navigateSafe(LanguageDialogDestination)
  }

  override fun openEncryptionStatePickerDialog() {
    navigateSafe(EncryptionStateDialogDestination)
  }

  override fun openCaloriesDialog() {
    navigateSafe(CaloriesDialogDestination)
  }

  override fun openRecipeInputIngredientScreen() {
    navigateSafe(RecipeInputIngredientScreenDestination)
  }

  override fun openIngredientDialog(ingredientId: String) {
    navigateSafe(IngredientDialogDestination(ingredientId))
  }

  override fun openRecipeInputCookingScreen() {
    navigateSafe(RecipeInputCookingScreenDestination)
  }

  override fun openSavedDialog() {
    navigateSafe(RecipeSavedDialogDestination)
  }

  override fun closeRecipeInput(recipeId: String?) {
    navController.popBackStack(NavGraphs.recipeInput.route, inclusive = true)
    if (recipeId != null &&
      navController.currentDestination?.route != RecipeScreenDestination(recipeId).route
    ) {
      openRecipeScreen(recipeId)
    }
  }

  override fun openCategoryInputScreen() = openCategoryInputScreen(categoryId = null)
  override fun openCategoryInputScreen(categoryId: String?) {
    navigateSafe(CategoryInputScreenDestination(categoryId = categoryId))
  }

  override fun openPurchaseInput(shoppingListId: String, purchaseId: String) {
    navigateSafe(PurchaseInputDialogDestination(shoppingListId, purchaseId))
  }

  override fun openShoppingListScreen() {
    navigateSafe(ShoppingListScreenDestination)
  }

  override fun navigateUp(skipAnimation: Boolean) {
    if (skipAnimation || !hideBottomSheet()) navController.navigateUp()
  }

  private fun navigateSafe(
    direction: Direction,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
  ) {
    ensureModalAnimationFinished()
    navController.navigate(direction, navOptionsBuilder)
  }

  private fun ensureModalAnimationFinished() {
    if (bottomSheet.navigatorSheetState.targetValue == ModalBottomSheetValue.Hidden &&
      bottomSheet.navigatorSheetState.currentValue != ModalBottomSheetValue.Hidden
    ) {
      navController.navigateUp()
    }
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
