package com.cactusknights.chefbook.domain.usecases.recipe

import android.content.res.Resources
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils.minutesToTimeString
import com.cactusknights.chefbook.core.ui.localizedName
import com.cactusknights.chefbook.domain.entities.recipe.Recipe
import com.cactusknights.chefbook.domain.entities.recipe.cooking.CookingItem
import com.cactusknights.chefbook.domain.entities.recipe.ingredient.IngredientItem
import javax.inject.Inject

interface IGetRecipeAsTextUseCase {
    suspend operator fun invoke(recipe: Recipe, resources: Resources): String
}

class GetRecipeAsTextUseCase @Inject constructor() : IGetRecipeAsTextUseCase {

    override suspend operator fun invoke(recipe: Recipe, resources: Resources): String {
        var text = recipe.name.uppercase()

        recipe.ownerName?.let { author ->
            text += "\n\n${resources.getString(R.string.common_general_author)}: $author"
        }

        recipe.description?.let { description ->
            text += "\n\n${resources.getString(R.string.common_general_description)}:\n$description"
        }

        if (recipe.servings != null || recipe.time != null) {
            text += "\n\n"
            recipe.servings?.let {
                text += "${resources.getString(R.string.common_general_servings)}: ${recipe.servings}"
            }
            recipe.time?.let {
                text += "\n${resources.getString(R.string.common_general_time)}: ${minutesToTimeString(recipe.time, resources)}"
            }
        }

        if (recipe.hasDietData()) {
            text += "\n\n${resources.getString(R.string.common_general_in_100_g)}:\n"
            recipe.calories?.let { calories ->
                text += "${resources.getString(R.string.common_general_calories)}: $calories ${resources.getString(R.string.common_general_kcal)}\n"
            }
            recipe.macronutrients?.protein?.let { protein ->
                text += "${resources.getString(R.string.common_general_protein)}: $protein\n"
            }
            recipe.macronutrients?.fats?.let { fats ->
                text += "${resources.getString(R.string.common_general_fats)}: $fats\n"
            }
            recipe.macronutrients?.carbohydrates?.let { carbohydrates ->
                text += "${resources.getString(R.string.common_general_carbs)}: $carbohydrates"
            }
        }

        text += "\n\n${resources.getString(R.string.common_general_ingredients).uppercase()}\n"
        for (ingredient in recipe.ingredients) {
            when (ingredient) {
                is IngredientItem.Section -> {
                    text += "${ingredient.name}:\n"
                }
                is IngredientItem.Ingredient -> {
                    text += "• ${ingredient.name}"
                    ingredient.amount?.let { amount ->
                        text += " - $amount"
                        ingredient.unit?.let { unit ->
                            text += " ${unit.localizedName(resources)}"
                        }
                    }
                    text += "\n"
                }
                else -> Unit
            }
        }
        text += "\n\n${resources.getString(R.string.common_general_cooking).uppercase()}\n"
        var stepCount = 1
        for (cookingItem in recipe.cooking) {
            when (cookingItem) {
                is CookingItem.Section -> {
                    text += "${cookingItem.name}:\n"
                }
                is CookingItem.Step -> {
                    text += "$stepCount. ${cookingItem.description}\n"
                    stepCount++
                }
                else -> Unit
            }
        }

        text += "\n\n${resources.getString(R.string.common_general_recipe)} #${recipe.id}, ${resources.getString(R.string.app_name)}"

        return text
    }

}
