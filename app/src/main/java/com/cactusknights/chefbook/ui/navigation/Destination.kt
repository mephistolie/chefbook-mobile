package com.cactusknights.chefbook.ui.navigation

import androidx.navigation.navDeepLink
import com.cactusknights.chefbook.core.Endpoints

const val CATEGORY_ID_ARGUMENT = "category_id"
const val RECIPE_ID_ARGUMENT = "recipe_id"

const val INGREDIENT_INDEX = "ingredient_index"

sealed class Destination(val route: String) {

    object Auth : Destination("auth")

    object Home : Destination("home") {

        object RecipeBook : Destination("${Home.route}/recipe_book") {

            object Search : Destination("${RecipeBook.route}/search")

            object Favourite : Destination("${RecipeBook.route}/favourite")

            object Category : Destination("${RecipeBook.route}/categories/{$CATEGORY_ID_ARGUMENT}") {
                fun route(
                    categoryId: Int
                ): String = route.replace("{$CATEGORY_ID_ARGUMENT}", categoryId.toString())
            }
        }

        object ShoppingList : Destination("shopping_list")

    }

    object Encryption : Destination("encryption")

    object RecipeInput : Destination("recipe_input?$RECIPE_ID_ARGUMENT={$RECIPE_ID_ARGUMENT}") {

        fun route(
            recipeId: Int? = null
        ): String = route.replace("{$RECIPE_ID_ARGUMENT}", recipeId.toString())

        object Details : Destination("${RecipeInput.route}/details") {

            object Visibility : Destination("${Details.route}/visibility")

            object Language : Destination("${Details.route}/language")

            object Encryption : Destination("${Details.route}/encryption")

            object Calories : Destination("${Details.route}/calories")

        }

        object Ingredients : Destination("${RecipeInput.route}/ingredients") {

            object Ingredient : Destination("${Ingredients.route}/{$INGREDIENT_INDEX}") {
                fun route(
                    ingredientIndex: Int
                ): String = route.replace("{$INGREDIENT_INDEX}", ingredientIndex.toString())
            }

        }

        object Cooking : Destination("${RecipeInput.route}/cooking")

    }

    object CommunityRecipes : Destination("community_recipes")

    object Recipe : Destination("recipe/{$RECIPE_ID_ARGUMENT}") {
        val deeplinks = Endpoints.recipesEndpointVariants.map { endpoint -> navDeepLink { uriPattern = "${endpoint}/{$RECIPE_ID_ARGUMENT}" } }
        fun route(recipeId: Int): String = route.replace("{$RECIPE_ID_ARGUMENT}", recipeId.toString())
    }
}
