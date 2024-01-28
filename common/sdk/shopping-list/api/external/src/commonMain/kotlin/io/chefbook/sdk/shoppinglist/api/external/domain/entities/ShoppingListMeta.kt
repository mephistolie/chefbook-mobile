package io.chefbook.sdk.shoppinglist.api.external.domain.entities

import io.chefbook.libs.models.profile.ProfileInfo

data class ShoppingListMeta(
  val id: String,
  val name: String? = null,
  val type: Type = Type.PERSONAL,
  val owner: ProfileInfo,
  val version: Int = 0,
) {

  enum class Type {
    PERSONAL, SHARED
  }
}
