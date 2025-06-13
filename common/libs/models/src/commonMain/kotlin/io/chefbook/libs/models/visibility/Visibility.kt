package io.chefbook.libs.models.visibility

enum class Visibility {
  Private,
  Link,
  Public;

  fun serialize(): String =
    when (this) {
      Private -> VISIBILITY_PRIVATE
      Link -> VISIBILITY_LINK
      Public -> VISIBILITY_PUBLIC
    }

  companion object {
    private const val VISIBILITY_PRIVATE = "private"
    private const val VISIBILITY_LINK = "link"
    private const val VISIBILITY_PUBLIC = "public"

    fun deserialize(visibility: String?): Visibility =
      when (visibility?.lowercase()) {
        VISIBILITY_PUBLIC -> Public
        VISIBILITY_LINK -> Link
        else -> Private
      }
  }
}
