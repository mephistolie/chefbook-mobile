package io.chefbook.sdk.recipe.crud.api.external.domain.entities

import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.measureunit.MeasureUnit
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

    open fun trim() = this

    data class Step(
      override val id: String = generateUUID(),
      val description: String = "",
      val time: Int? = null,
      val pictures: List<Picture> = emptyList(),
      val recipeId: String? = null,
    ) : CookingItem(id) {

      override fun trim() = copy(description = description.trim())
    }

    data class Section(
      override val id: String,
      val name: String,
    ) : CookingItem(id) {

      override fun trim() = copy(name = name.trim())
    }
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

  fun trim() =
    copy(
      name = name.trim(),
      description = description?.trim(),
      ingredients = ingredients.map { it.trim() },
      cooking = cooking.map(CookingItem::trim),
    )

  private fun IngredientsItem.trim() =
    when (this) {
      is IngredientsItem.Ingredient -> copy(
        name = name.trim(),
        measureUnit = (measureUnit as? MeasureUnit.Custom)?.let { MeasureUnit.Custom(it.name.trim()) } ?: measureUnit
      )
      is IngredientsItem.Section -> copy(name = name.trim())
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

      ingredients = listOf(IngredientsItem.Ingredient(id = generateUUID(), name = "")),
      cooking =  listOf(CookingItem.Step()),

      version = 1,
    )
  }
}
