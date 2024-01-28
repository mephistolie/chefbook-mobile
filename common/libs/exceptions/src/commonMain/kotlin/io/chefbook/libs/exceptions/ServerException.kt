package io.chefbook.libs.exceptions

data class ServerException(
  val code: Int,
  val type: String,
  override val message: String? = null,
) : Exception(message) {

  val isClientSide: Boolean
    get() = code in 400..499

  val isServerSide: Boolean
    get() = code in 500..599

  val isVersionsConflict: Boolean
    get() = code == 409

  companion object {
    const val INVALID_BODY = "invalid_body"
    const val BIG_FILE = "big_file"

    const val UNAUTHORIZED = "unauthorized"
    const val INVALID_CREDENTIALS = "invalid_credentials"
    const val INVALID_ACTIVATION_LINK = "invalid_activation_link"

    const val ACCESS_DENIED = "access_denied"
    const val USER_BLOCKED = "user_blocked"
    const val USER_EXISTS = "user_exists"

    const val NOT_FOUND = "not_found"
  }
}
