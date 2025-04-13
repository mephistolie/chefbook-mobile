package io.chefbook.libs.models.visibility

enum class Visibility {
  PRIVATE,
  LINK,
  PUBLIC;

  fun serialize(): String =
    when (this) {
      PRIVATE -> VISIBILITY_PRIVATE
      LINK -> VISIBILITY_LINK
      PUBLIC -> VISIBILITY_PUBLIC
    }

  companion object {
    private const val VISIBILITY_PRIVATE = "private"
    private const val VISIBILITY_LINK = "link"
    private const val VISIBILITY_PUBLIC = "public"

    fun deserialize(visibility: String?): Visibility =
      when (visibility) {
        VISIBILITY_PUBLIC -> PUBLIC
        VISIBILITY_LINK -> LINK
        else -> PRIVATE
      }
  }
}
