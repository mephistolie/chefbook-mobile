package io.chefbook.sdk.recipe.crud.api.external.domain.entities

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.utils.uuid.generateUUID
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Decrypted.IngredientsItem
import io.chefbook.sdk.recipe.core.api.external.domain.entities.Recipe.Macronutrients
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility

data class RecipeInput(
  val id: String,
  val name: String,
  val visibility: Visibility,
  val hasEncryption: Boolean,
  val language: Language,
  val description: String?,
  val preview: Picture?,

  val servings: Int?,
  val time: Int?,

  val calories: Int?,
  val macronutrients: Macronutrients?,

  val ingredients: List<IngredientsItem>,
  val cooking: List<CookingItem>,

  val version: Int,
) {

  val pictures
    get() = Pictures(
      preview = preview,
      cooking = cooking.filterIsInstance<CookingItem.Step>()
        .filter { it.pictures.isNotEmpty() }.associate { it.id to it.pictures },
    )

  sealed class CookingItem(
    open val id: String,
  ) {
    data class Step(
      override val id: String,
      val description: String,
      val time: Int? = null,
      val pictures: List<Picture> = emptyList(),
      val recipeId: String? = null,
    ) : CookingItem(id)

    data class Section(
      override val id: String,
      val name: String,
    ) : CookingItem(id)
  }

  data class Pictures(
    val preview: Picture? = null,
    val cooking: Map<String, List<Picture>> = emptyMap(),
  ) {
    val pendingCount
      get(): Int {
        var count = 0
        (preview as? Picture.Pending)?.let { count += 1 }
        cooking.values
          .forEach { pictures ->
            pictures.map { (it as? Picture.Pending)?.let { count += 1 } }
          }
        return count
      }
  }

  sealed class Picture(open val path: String) {
    data class Uploaded(override val path: String) : Picture(path)
    data class Pending(val source: String) : Picture(source)
  }

  companion object {
    fun new(language: Language = Language.ENGLISH) = RecipeInput(
      id = generateUUID(),
      name = "",
      visibility = Visibility.PRIVATE,
      hasEncryption = false,
      language = language,
      description = null,
      preview = null,

      servings = 1,
      time = null,

      calories = null,
      macronutrients = null,

      ingredients = emptyList(),
      cooking = emptyList(),

      version = 1,
    )
  }
}
