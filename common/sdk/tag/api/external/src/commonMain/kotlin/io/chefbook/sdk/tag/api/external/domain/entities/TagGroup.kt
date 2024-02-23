package io.chefbook.sdk.tag.api.external.domain.entities

data class TagGroup(
  val id: String,
  val name: String,
) {

  companion object {
    const val MENU = "menu"
    const val CUISINE = "cuisine"
  }
}
