package io.chefbook.libs.utils.auth

fun isEmail(email: String): Boolean {
  val atIndex = email.indexOf("@")
  return atIndex >= 0 && atIndex < email.indexOf(".") && email.length >= 5
}

fun validatePassword(password: String, repeatPassword: String): PasswordRating {

  val numberValidator = """^(?=.*[0-9]).{8,}$""".toRegex()
  val lowerValidator = """^(?=.*[a-z]).{8,}$""".toRegex()
  val upperValidator = """^(?=.*[A-Z]).{8,}$""".toRegex()
  val spaceValidator = """^(?=\S+$).+""".toRegex()

  if (password.isNotEmpty() && !password.matches(spaceValidator)) return PasswordRating.SPACE
  if (password.length < 8) return PasswordRating.SHORT
  if (!password.matches(lowerValidator)) return PasswordRating.LOWER
  if (!password.matches(upperValidator)) return PasswordRating.UPPER
  if (!password.matches(numberValidator)) return PasswordRating.NUMBER
  if (password != repeatPassword) return PasswordRating.NOT_MATCH

  return PasswordRating.VALID
}

enum class PasswordRating {
  VALID, SHORT, UPPER, LOWER, NUMBER, SPACE, NOT_MATCH
}
