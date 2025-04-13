package io.chefbook.sdk.recipe.core.api.external.domain.entities

typealias DecryptedRecipeInfo = RecipeInfo.Decrypted
typealias EncryptedRecipeInfo = RecipeInfo.Encrypted

sealed interface RecipeInfo : RecipeMeta {
  val meta: RecipeMeta

  val name: String
  val preview: String?

  val isOwned: Boolean
  val isSaved: Boolean

  val collections: List<CollectionInfo>
  val isFavourite: Boolean

  val servings: Int?
  val time: Int?

  val calories: Int?

  val isEncrypted: Boolean
  val isDecrypted
    get() = !isEncrypted

  fun withSavedStatus(isSaved: Boolean): RecipeInfo
  fun withCollections(collections: List<CollectionInfo>): RecipeInfo
  fun withFavouriteStatus(isFavourite: Boolean): RecipeInfo

  override fun withId(id: String): RecipeInfo
  override fun withScore(score: Int?): RecipeInfo
  override fun withVersion(version: Int): RecipeInfo

  data class Decrypted(
      override val meta: RecipeMeta,

      override val isOwned: Boolean,
      override val isSaved: Boolean,

      override val collections: List<CollectionInfo>,
      override val isFavourite: Boolean,

      override val servings: Int?,
      override val time: Int?,

      override val calories: Int?,

      override val name: String,
      override val preview: String?,
  ) : RecipeInfo, RecipeMeta by meta {

    override val isEncrypted = false

    override fun withSavedStatus(isSaved: Boolean) = copy(
      isSaved = isSaved,
      isFavourite = isFavourite && isSaved,
    )
    override fun withCollections(collections: List<CollectionInfo>) = copy(collections = collections)
    override fun withFavouriteStatus(isFavourite: Boolean) = copy(isFavourite = isFavourite)

    override fun withId(id: String) = copy(meta = meta.withId(id))
    override fun withScore(score: Int?) = copy(meta = meta.withScore(score))
    override fun withVersion(version: Int) = copy(meta = meta.withVersion(version))
  }

  data class Encrypted(
      override val meta: RecipeMeta,

      override val isOwned: Boolean,
      override val isSaved: Boolean,

      override val collections: List<CollectionInfo>,
      override val isFavourite: Boolean,

      override val servings: Int?,
      override val time: Int?,

      override val calories: Int?,

      override val name: String,
      override val preview: String?,
  ) : RecipeInfo, RecipeMeta by meta {

    override val isEncrypted = true

    override fun withSavedStatus(isSaved: Boolean) = copy(
      isSaved = isSaved,
      isFavourite = isFavourite && isSaved,
    )
    override fun withCollections(collections: List<CollectionInfo>) = copy(collections = collections)
    override fun withFavouriteStatus(isFavourite: Boolean) = copy(isFavourite = isFavourite)

    override fun withId(id: String) = copy(meta = meta.withId(id))
    override fun withScore(score: Int?): EncryptedRecipeInfo = copy(meta = meta.withScore(score))
    override fun withVersion(version: Int) = copy(meta = meta.withVersion(version))
  }
}