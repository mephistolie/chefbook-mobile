package io.chefbook.sdk.tag.api.external.domain.entities

data class TagGroup(
  val id: String,
  val name: String,
) {

  companion object {
    const val MENU = "menu"
    const val FOOD_TYPE = "food_type"
    const val MEAL_TIME = "meal_time"
    const val CUISINE = "cuisine"
  }
}
