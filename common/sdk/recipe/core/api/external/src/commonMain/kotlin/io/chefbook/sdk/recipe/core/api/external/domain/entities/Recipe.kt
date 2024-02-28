package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.libs.models.measureunit.MeasureUnit
import io.chefbook.sdk.category.api.external.domain.entities.Category

typealias DecryptedRecipe = Recipe.Decrypted
typealias EncryptedRecipe = Recipe.Encrypted

sealed class Recipe(
  open val info: RecipeInfo,
  open val macronutrients: Macronutrients?,
) {

  val id
    get() = info.id

  val owner
    get() = info.owner

  val isOwned
    get() = info.isOwned
  val isSaved
    get() = info.isSaved
  val visibility
    get() = info.visibility
  val isEncryptionEnabled
    get() = info.isEncryptionEnabled

  val language
    get() = info.language

  val version
    get() = info.version
  val creationTimestamp
    get() = info.creationTimestamp
  val updateTimestamp
    get() = info.updateTimestamp

  val rating
    get() = info.rating

  val tags
    get() = info.tags
  val categories
    get() = info.categories
  val isFavourite
    get() = info.isFavourite

  val servings
    get() = info.servings
  val time
    get() = info.time

  val calories
    get() = info.calories

  val hasDietData
    get() = calories != null ||
            macronutrients?.protein != null ||
            macronutrients?.fats != null ||
            macronutrients?.carbohydrates != null

  val isEncrypted
    get() = info.isEncrypted
  val isDecrypted
    get() = info.isDecrypted

  abstract fun withSavedStatus(isSaved: Boolean): Recipe
  abstract fun withCategories(categories: List<Category>): Recipe
  abstract fun withFavouriteStatus(isFavourite: Boolean): Recipe

  abstract fun withId(id: String): Recipe
  abstract fun withScore(score: Int?): Recipe
  abstract fun withVersion(version: Int): Recipe

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

    val name
      get() = info.name

    val preview
      get() = info.preview

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

    val name
      get() = info.name

    val preview
      get() = info.preview

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