package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.sdk.category.api.external.domain.entities.Category

typealias DecryptedRecipeInfo = RecipeInfo.Decrypted
typealias EncryptedRecipeInfo = RecipeInfo.Encrypted

sealed class RecipeInfo(
  open val meta: RecipeMeta,

  open val name: String,
  open val preview: String?,

  open val isOwned: Boolean,
  open val isSaved: Boolean,

  open val categories: List<Category>,
  open val isFavourite: Boolean,

  open val servings: Int?,
  open val time: Int?,

  open val calories: Int?,
) {
  val id
    get() = meta.id

  val owner
    get() = meta.owner

  val visibility
    get() = meta.visibility
  val isEncryptionEnabled
    get() = meta.isEncryptionEnabled

  val language
    get() = meta.language

  val version
    get() = meta.version
  val creationTimestamp
    get() = meta.creationTimestamp
  val updateTimestamp
    get() = meta.updateTimestamp

  val rating
    get() = meta.rating


  abstract val isEncrypted: Boolean
  val isDecrypted
    get() = !isEncrypted

  abstract fun withSavedStatus(isSaved: Boolean): RecipeInfo
  abstract fun withCategories(categories: List<Category>): RecipeInfo
  abstract fun withFavouriteStatus(isFavourite: Boolean): RecipeInfo

  abstract fun withId(id: String): RecipeInfo
  abstract fun withScore(score: Int?): RecipeInfo
  abstract fun withVersion(version: Int): RecipeInfo

  data class Decrypted(
    override val meta: RecipeMeta,

    override val isOwned: Boolean,
    override val isSaved: Boolean,

    override val categories: List<Category>,
    override val isFavourite: Boolean,

    override val servings: Int?,
    override val time: Int?,

    override val calories: Int?,

    override val name: String,
    override val preview: String?,
  ) : RecipeInfo(
    meta = meta,

    name = name,
    preview = preview,

    isOwned = isOwned,
    isSaved = isSaved,

    categories = categories,
    isFavourite = isFavourite,

    servings = servings,
    time = time,

    calories = calories,
  ) {

    override val isEncrypted = false

    override fun withSavedStatus(isSaved: Boolean) = copy(isSaved = isSaved)
    override fun withCategories(categories: List<Category>) = copy(categories = categories)
    override fun withFavouriteStatus(isFavourite: Boolean) = copy(isFavourite = isFavourite)

    override fun withId(id: String) = copy(meta = meta.withId(id))
    override fun withScore(score: Int?) = copy(meta = meta.withScore(score))
    override fun withVersion(version: Int) = copy(meta = meta.withVersion(version))
  }

  data class Encrypted(
    override val meta: RecipeMeta,

    override val isOwned: Boolean,
    override val isSaved: Boolean,

    override val categories: List<Category>,
    override val isFavourite: Boolean,

    override val servings: Int?,
    override val time: Int?,

    override val calories: Int?,

    override val name: String,
    override val preview: String?,
  ) : RecipeInfo(
    meta = meta,

    name = name,
    preview = preview,

    isOwned = isOwned,
    isSaved = isSaved,

    categories = categories,
    isFavourite = isFavourite,

    servings = servings,
    time = time,

    calories = calories,
  ) {
    override val isEncrypted = true

    override fun withSavedStatus(isSaved: Boolean) = copy(isSaved = isSaved)
    override fun withCategories(categories: List<Category>) = copy(categories = categories)
    override fun withFavouriteStatus(isFavourite: Boolean) = copy(isFavourite = isFavourite)

    override fun withId(id: String) = copy(meta = meta.withId(id))
    override fun withScore(score: Int?): EncryptedRecipeInfo = copy(meta = meta.withScore(score))
    override fun withVersion(version: Int) = copy(meta = meta.withVersion(version))
  }
}