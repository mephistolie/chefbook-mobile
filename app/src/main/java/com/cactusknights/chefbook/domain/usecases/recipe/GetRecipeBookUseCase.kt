package com.cactusknights.chefbook.domain.usecases.recipe

import com.cactusknights.chefbook.domain.entities.common.Sorting
import com.cactusknights.chefbook.domain.entities.recipe.RecipeBookFilter
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.domain.interfaces.IRecipeRepo

interface IGetRecipeBookUseCase {
    suspend operator fun invoke(
        filter: RecipeBookFilter = RecipeBookFilter(),
    ): List<RecipeInfo>
}

class GetRecipeBookUseCase(
    private val recipesRepo: IRecipeRepo,
) : IGetRecipeBookUseCase {

    override suspend operator fun invoke(
        filter: RecipeBookFilter,
    ): List<RecipeInfo> {
        var recipes = recipesRepo.getRecipeBook()

        filter.search?.let { query -> recipes = recipes.filter { query.lowercase() in it.name.lowercase() } }
        if (filter.onlyFavourite) recipes = recipes.filter { it.isFavourite }
        filter.targetCategoryId?.let { categoryId ->
            recipes = recipes.filter { recipe -> categoryId in recipe.categories.map { it.id } }
        }

        recipes = when (filter.sortBy) {
            Sorting.NAME -> recipes.sortedBy { it.name }
            Sorting.CREATION -> recipes.sortedBy { it.name }
            Sorting.LIKES -> recipes.sortedBy { it.name }
            Sorting.TIME -> recipes.sortedBy { it.name }
            Sorting.SERVINGS -> recipes.sortedBy { it.name }
            Sorting.CALORIES -> recipes.sortedBy { it.name }
        }

        return recipes
    }

}
