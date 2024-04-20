package io.chefbook.sdk.recipe.core.api.external.domain.entities

import io.chefbook.sdk.category.api.external.domain.entities.Category

typealias DecryptedRecipeInfo = RecipeInfo.Decrypted
typealias EncryptedRecipeInfo = RecipeInfo.Encrypted

sealed interface RecipeInfo {
  val meta: RecipeMeta

  val name: String
  val preview: String?

  val isOwned: Boolean
  val isSaved: Boolean

  val categories: List<Category>
  val isFavourite: Boolean

  val servings: Int?
  val time: Int?

  val calories: Int?

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

  val tags
    get() = meta.tags


  val isEncrypted: Boolean
  val isDecrypted
    get() = !isEncrypted

  fun withSavedStatus(isSaved: Boolean): RecipeInfo
  fun withCategories(categories: List<Category>): RecipeInfo
  fun withFavouriteStatus(isFavourite: Boolean): RecipeInfo

  fun withId(id: String): RecipeInfo
  fun withScore(score: Int?): RecipeInfo
  fun withVersion(version: Int): RecipeInfo

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
  ) : RecipeInfo {

    override val isEncrypted = false

    override fun withSavedStatus(isSaved: Boolean) = copy(
      isSaved = isSaved,
      isFavourite = if (isSaved) isFavourite else false,
      categories = if (isSaved) categories else emptyList(),
    )

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
  ) : RecipeInfo {
    override val isEncrypted = true

    override fun withSavedStatus(isSaved: Boolean) = copy(
      isSaved = isSaved,
      isFavourite = if (isSaved) isFavourite else false,
      categories = if (isSaved) categories else emptyList(),
    )
    override fun withCategories(categories: List<Category>) = copy(categories = categories)
    override fun withFavouriteStatus(isFavourite: Boolean) = copy(isFavourite = isFavourite)

    override fun withId(id: String) = copy(meta = meta.withId(id))
    override fun withScore(score: Int?): EncryptedRecipeInfo = copy(meta = meta.withScore(score))
    override fun withVersion(version: Int) = copy(meta = meta.withVersion(version))
  }
}