package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.libs.models.measureunit.MeasureUnit

typealias DecryptedRecipe = Recipe.Decrypted
typealias EncryptedRecipe = Recipe.Encrypted

sealed interface Recipe : RecipeInfoDelegate {

  val info: RecipeInfo
  val macronutrients: Macronutrients?

  val hasDietData
    get() = calories != null ||
        macronutrients?.protein != null ||
        macronutrients?.fats != null ||
        macronutrients?.carbohydrates != null

  fun withSavedStatus(isSaved: Boolean): Recipe
  fun withCollections(collections: List<CollectionInfo>): Recipe
  fun withFavouriteStatus(isFavourite: Boolean): Recipe

  fun withId(id: String): Recipe
  fun withScore(score: Int?): Recipe
  fun withVersion(version: Int): Recipe

  data class Decrypted(
    override val info: DecryptedRecipeInfo,
    override val macronutrients: Macronutrients?,

    val description: String?,
    val ingredients: List<IngredientsItem>,
    val cooking: List<CookingItem>,
  ) : Recipe, RecipeInfoDelegate by info {

    override fun withSavedStatus(isSaved: Boolean) =
      copy(info = info.withSavedStatus(isSaved))

    override fun withCollections(collections: List<CollectionInfo>) =
      copy(info = info.withCollections(collections))

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
  ) : Recipe, RecipeInfoDelegate by info {

    override fun withSavedStatus(isSaved: Boolean) =
      copy(info = info.withSavedStatus(isSaved))

    override fun withCollections(collections: List<CollectionInfo>) =
      copy(info = info.withCollections(collections))

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
