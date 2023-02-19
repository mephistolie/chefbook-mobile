package com.cactusknights.chefbook.navigation.graphs

import com.mysty.chefbook.features.about.ui.destinations.AboutScreenDestination
import com.mysty.chefbook.features.auth.ui.destinations.AuthScreenDestination
import com.mysty.chefbook.features.category.ui.input.destinations.CategoryInputDialogDestination
import com.mysty.chefbook.features.encryption.ui.vault.destinations.EncryptedVaultScreenDestination
import com.mysty.chefbook.features.home.ui.destinations.HomeScreenDestination
import com.mysty.chefbook.features.purchases.input.ui.destinations.PurchaseInputDialogDestination
import com.mysty.chefbook.features.recipe.info.ui.destinations.RecipeScreenDestination
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
import com.mysty.chefbook.features.recipebook.favourite.ui.destinations.FavouriteRecipesScreenDestination
import com.mysty.chefbook.features.recipebook.search.ui.destinations.RecipeBookSearchScreenDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.DismissibleOneButtonDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.DismissibleTwoButtonsDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.NonDismissibleOneButtonDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.NonDismissibleTwoButtonsDialogDestination
import com.mysty.chefbook.ui.common.dialogs.destinations.PicturesViewerDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

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
    override val startRoute = HomeScreenDestination

    override val destinationsByRoute = listOf<DestinationSpec<*>>(
      AuthScreenDestination,
      EncryptedVaultScreenDestination,
      HomeScreenDestination,
      RecipeBookSearchScreenDestination,
      FavouriteRecipesScreenDestination,
      CategoryRecipesScreenDestination,
      PurchaseInputDialogDestination,
      AboutScreenDestination,

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