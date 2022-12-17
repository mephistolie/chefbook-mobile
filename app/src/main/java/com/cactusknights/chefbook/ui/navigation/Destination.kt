package com.cactusknights.chefbook.ui.navigation

import androidx.navigation.navDeepLink
import com.cactusknights.chefbook.ui.screens.recipe.models.RecipeScreenTab
import com.mysty.chefbook.api.common.constants.Endpoints

const val CATEGORY_ID_ARGUMENT = "category_id"
const val RECIPE_ID_ARGUMENT = "recipe_id"
const val RECIPE_TAB_ARGUMENT = "recipe_tab"
const val CLOSE_ON_UNLOCKED_ARGUMENT = "close_on_unlocked"

const val INGREDIENT_ID = "ingredient_id"

sealed class Destination(val route: String) {

    object Auth : Destination("auth")

    object Home : Destination("home") {

        object RecipeBook : Destination("${Home.route}/recipe_book") {

            object Search : Destination("${RecipeBook.route}/search")

            object Favourite : Destination("${RecipeBook.route}/favourite")

            object Category : Destination("${RecipeBook.route}/categories/{$CATEGORY_ID_ARGUMENT}") {
                fun route(
                    categoryId: String
                ): String = route.replace("{$CATEGORY_ID_ARGUMENT}", categoryId)
            }
        }

        object ShoppingList : Destination("shopping_list")

    }

    object About : Destination("about")

    object Encryption : Destination("encryption?$CLOSE_ON_UNLOCKED_ARGUMENT={$CLOSE_ON_UNLOCKED_ARGUMENT}") {

        fun route(
            closeOnUnlocked: Boolean = false
        ): String = route.replace("{$CLOSE_ON_UNLOCKED_ARGUMENT}", closeOnUnlocked.toString())

    }

    object RecipeInput : Destination("recipe_input?$RECIPE_ID_ARGUMENT={$RECIPE_ID_ARGUMENT}") {

        fun route(
            recipeId: String? = null
        ): String = if (recipeId != null) route.replace("{$RECIPE_ID_ARGUMENT}", recipeId) else route.substringBefore("?")

        object Details : Destination("${RecipeInput.route}/details") {

            object Visibility : Destination("${Details.route}/visibility")

            object Language : Destination("${Details.route}/language")

            object Encryption : Destination("${Details.route}/encryption")

            object Calories : Destination("${Details.route}/calories")

        }

        object Ingredients : Destination("${RecipeInput.route}/ingredients") {

            object Ingredient : Destination("${Ingredients.route}/{$INGREDIENT_ID}") {
                fun route(
                    ingredientId: String
                ): String = route.replace("{$INGREDIENT_ID}", ingredientId)
            }

        }

        object Cooking : Destination("${RecipeInput.route}/cooking")

    }

    object CommunityRecipes : Destination("community_recipes")

    object Recipe : Destination("recipe/{$RECIPE_ID_ARGUMENT}?$RECIPE_TAB_ARGUMENT={$RECIPE_TAB_ARGUMENT}") {
        val deeplinks = Endpoints.recipesEndpointVariants.map { endpoint -> navDeepLink { uriPattern = "${endpoint}/{$RECIPE_ID_ARGUMENT}?$RECIPE_TAB_ARGUMENT={$RECIPE_TAB_ARGUMENT}" } }
        fun route(
            recipeId: String,
            defaultTab: RecipeScreenTab = RecipeScreenTab.Details,
        ): String = route
            .replace("{$RECIPE_ID_ARGUMENT}", recipeId)
            .replace("{$RECIPE_TAB_ARGUMENT}", defaultTab.id)
    }
}
