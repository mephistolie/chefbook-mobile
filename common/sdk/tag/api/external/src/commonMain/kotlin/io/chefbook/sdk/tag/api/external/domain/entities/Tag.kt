package io.chefbook.sdk.tag.api.external.domain.entities

data class Tag(
  val id: String,
  val name: String,
  val emoji: String? = null,
  val group: TagGroup? = null,
) {

  companion object {
    const val VEGAN_FOOD = "vegan_food"
    const val VEGETARIAN_FOOD = "vegetarian_food"
    const val PROPER_NUTRITION = "proper_nutrition"
    const val WEIGHT_GAIN = "weight_gain"
    const val SPICY_FOOD = "spicy_food"
  }
}
