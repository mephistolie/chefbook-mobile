package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.sdk.category.api.external.domain.entities.Category

typealias DecryptedRecipe = Recipe.Decrypted
typealias EncryptedRecipe = Recipe.Encrypted

sealed class Recipe(
  open val info: RecipeInfo,
  open val macronutrients: Macronutrients?,
) : RecipeInfo by info {

  val hasDietData
    get() = calories != null ||
            macronutrients?.protein != null ||
            macronutrients?.fats != null ||
            macronutrients?.carbohydrates != null

  abstract override fun withSavedStatus(isSaved: Boolean): Recipe
  abstract override fun withCategories(categories: List<Category>): Recipe
  abstract override fun withFavouriteStatus(isFavourite: Boolean): Recipe

  abstract override fun withId(id: String): Recipe
  abstract override fun withScore(score: Int?): Recipe
  abstract override fun withVersion(version: Int): Recipe

  data class Decrypted(
    override val info: DecryptedRecipeInfo,
    override val macronutrients: Macronutrients?,

    val description: String?,
    val ingredients: List<IngredientsItem>,
    val cooking: List<CookingItem>,
  ) : Recipe(
    info = info,
    macronutrients = macronutrients,
  ) {

    override fun withSavedStatus(isSaved: Boolean) = copy(info = info.withSavedStatus(isSaved))
    override fun withCategories(categories: List<Category>) =
      copy(info = info.withCategories(categories))

    override fun withFavouriteStatus(isFavourite: Boolean) =
      copy(info = info.withFavouriteStatus(isFavourite))

    override fun withId(id: String): Recipe = copy(info = info.withId(id))
    override fun withScore(score: Int?) = copy(info = info.withScore(score))
    override fun withVersion(version: Int) = copy(info = info.withVersion(version))

    sealed class IngredientsItem(
      open val id: String
    ) {

      data class Ingredient(
        override val id: String,
        val name: String,
        val amount: Float? = null,
        val measureUnit: MeasureUnit? = null,
        val recipeId: String? = null,
      ) : IngredientsItem(id)

      data class Section(
        override val id: String,
        val name: String,
      ) : IngredientsItem(id)
    }

    sealed class CookingItem(
      open val id: String
    ) {

      data class Step(
        override val id: String,
        val description: String,
        val time: Int?,
        val pictures: List<String>,
        val recipeId: String?,
      ) : CookingItem(id)

      data class Section(
        override val id: String,
        val name: String,
      ) : CookingItem(id)
    }
  }

  data class Encrypted(
    override val info: EncryptedRecipeInfo,
    override val macronutrients: Macronutrients?,

    val description: String?,
    val ingredients: String,
    val cooking: String,
    val cookingPictures: Map<String, List<String>>,
  ) : Recipe(
    info = info,
    macronutrients = macronutrients,
  ) {

    override fun withSavedStatus(isSaved: Boolean) = copy(info = info.withSavedStatus(isSaved))
    override fun withCategories(categories: List<Category>) =
      copy(info = info.withCategories(categories))

    override fun withFavouriteStatus(isFavourite: Boolean) =
      copy(info = info.withFavouriteStatus(isFavourite))

    override fun withId(id: String): Recipe = copy(info = info.withId(id))
    override fun withScore(score: Int?) = copy(info = info.withScore(score))
    override fun withVersion(version: Int) = copy(info = info.withVersion(version))
  }

  data class Macronutrients(
    val protein: Int? = null,
    val fats: Int? = null,
    val carbohydrates: Int? = null,
  )
}