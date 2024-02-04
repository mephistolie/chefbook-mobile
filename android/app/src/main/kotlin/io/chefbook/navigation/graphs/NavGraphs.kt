package io.chefbook.navigation.graphs

import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import io.chefbook.features.about.ui.destinations.AboutScreenDestination
import io.chefbook.features.auth.ui.destinations.AuthScreenDestination
import io.chefbook.features.category.ui.input.destinations.CategoryInputDialogDestination
import io.chefbook.features.encryption.ui.vault.destinations.EncryptedVaultScreenDestination
import io.chefbook.features.profile.control.ui.destinations.ProfileScreenDestination
import io.chefbook.features.profile.editing.ui.destinations.ProfileEditingScreenDestination
import io.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
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
import io.chefbook.features.recipebook.dashboard.ui.destinations.DashboardScreenDestination
import io.chefbook.features.recipebook.favourite.ui.destinations.FavouriteRecipesScreenDestination
import io.chefbook.features.recipebook.search.ui.destinations.RecipeBookSearchScreenDestination
import io.chefbook.features.settings.ui.destinations.SettingsScreenDestination
import io.chefbook.features.shoppinglist.control.ui.screen.destinations.ShoppingListScreenDestination
import io.chefbook.features.shoppinglist.purchases.input.ui.destinations.PurchaseInputDialogDestination
import io.chefbook.ui.common.dialogs.destinations.DismissibleOneButtonDialogDestination
import io.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import io.chefbook.ui.common.dialogs.destinations.NonDismissibleOneButtonDialogDestination
import io.chefbook.ui.common.dialogs.destinations.NonDismissibleTwoButtonsDialogDestination
import io.chefbook.ui.common.dialogs.destinations.PicturesViewerDestination

object NavGraphs {

  const val RECIPE_ID_ARGUMENT = "recipeId"

  val recipe = object : NavGraphSpec {

    override val route = "recipe/{$RECIPE_ID_ARGUMENT}"
    override val startRoute: Route = RecipeScreenDestination

    override val destinationsByRoute = listOf<DestinationSpec<*>>(
      RecipeScreenDestination,
      RecipeShareDialogDestination
    )
      .associateBy { it.route }
  }

  val recipeInput = object : NavGraphSpec {

    override val route = "recipe_input?$RECIPE_ID_ARGUMENT={$RECIPE_ID_ARGUMENT}"
    override val startRoute = RecipeInputDetailsScreenDestination

    override val destinationsByRoute = listOf<DestinationSpec<*>>(
      RecipeInputDetailsScreenDestination,
      VisibilityDialogDestination,
      LanguageDialogDestination,
      EncryptionStateDialogDestination,
      CaloriesDialogDestination,

      RecipeInputIngredientScreenDestination,
      IngredientDialogDestination,

      RecipeInputCookingScreenDestination,

      RecipeSavedDialogDestination,
    )
      .associateBy { it.route }
  }

  fun recipeInput(recipeId: String? = null): String =
    if (recipeId != null)
      recipeInput.route.replace("{$RECIPE_ID_ARGUMENT}", recipeId.toString())
    else "recipe_input"

  val root = object : NavGraphSpec {
    override val route = "root"
    override val startRoute = DashboardScreenDestination

    override val destinationsByRoute = listOf<DestinationSpec<*>>(
      AuthScreenDestination,
      DashboardScreenDestination,
      ProfileScreenDestination,
      ProfileEditingScreenDestination,
      EncryptedVaultScreenDestination,
      RecipeBookSearchScreenDestination,
      FavouriteRecipesScreenDestination,
      CategoryRecipesScreenDestination,
      PurchaseInputDialogDestination,
      SettingsScreenDestination,
      AboutScreenDestination,
      ShoppingListScreenDestination,

      DismissibleOneButtonDialogDestination,
      NonDismissibleOneButtonDialogDestination,
      DismissibleTwoButtonsDialogDestination,
      NonDismissibleTwoButtonsDialogDestination,
      PicturesViewerDestination,
      CategoryInputDialogDestination,
    )
      .associateBy { it.route }

    override val nestedNavGraphs = listOf(recipe, recipeInput)
  }
}